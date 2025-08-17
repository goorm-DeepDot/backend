package project.deepdot.mainpage.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.deepdot.mainpage.api.dto.FocusDailyResponse;
import project.deepdot.mainpage.api.dto.FocusDailyUpsertRequest;
import project.deepdot.mainpage.api.dto.FocusWeekResponse;
import project.deepdot.mainpage.domain.FocusDaily;
import project.deepdot.mainpage.domain.FocusDailyRepository;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FocusService {

    private final FocusDailyRepository repo;

    @Transactional
    public FocusDailyResponse upsertDaily(Long userId, FocusDailyUpsertRequest req) {
        String zone = (req.getZoneId() == null || req.getZoneId().isBlank()) ? "Asia/Seoul" : req.getZoneId();

        LocalDate date = LocalDate.parse(req.getLocalDate()); // yyyy-MM-dd

        int totalMinutes = computeTotalMinutes(req);
        if (totalMinutes < 0 || totalMinutes > 24 * 60) {
            throw new IllegalArgumentException("totalMinutes는 0~1440 범위여야 합니다.");
        }

        FocusDaily entity = repo.findByUserIdAndLocalDate(userId, date)
                .orElseGet(() -> FocusDaily.builder()
                        .userId(userId)
                        .localDate(date)
                        .minutes(0)
                        .zoneId(zone)
                        .build());

        entity.updateMinutes(totalMinutes, zone);
        repo.save(entity);

        return toDailyResponse(entity);
    }

    @Transactional(readOnly = true)
    public FocusWeekResponse weekly(Long userId, String anchorDateStr, String zoneId) {
        String zone = (zoneId == null || zoneId.isBlank()) ? "Asia/Seoul" : zoneId;
        ZoneId zid = ZoneId.of(zone);

        LocalDate anchor = (anchorDateStr == null || anchorDateStr.isBlank())
                ? LocalDate.now(zid)
                : LocalDate.parse(anchorDateStr);

        LocalDate weekStart = anchor.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate weekEnd   = weekStart.plusDays(6);

        // DB에서 이 구간 조회
        List<FocusDaily> rows = repo.findByUserIdAndLocalDateBetweenOrderByLocalDateAsc(userId, weekStart, weekEnd);

        // 날짜 → minutes 맵
        Map<LocalDate, Integer> byDate = rows.stream()
                .collect(Collectors.toMap(FocusDaily::getLocalDate, FocusDaily::getMinutes));

        // 월~일 7칸 채우기(없으면 0)
        List<FocusWeekResponse.Day> days = new ArrayList<>();
        int sum = 0;
        for (int i = 0; i < 7; i++) {
            LocalDate d = weekStart.plusDays(i);
            int minutes = byDate.getOrDefault(d, 0);
            sum += minutes;
            days.add(new FocusWeekResponse.Day(
                    d.toString(),
                    d.getDayOfWeek().getValue(),        // 1=월..7=일
                    minutes,
                    minutes / 60,
                    minutes % 60
            ));
        }

        return new FocusWeekResponse(
                weekStart.toString(),
                weekEnd.toString(),
                sum,
                sum / 60,
                sum % 60,
                days
        );
    }

    private int computeTotalMinutes(FocusDailyUpsertRequest req) {
        if (req.getTotalMinutes() != null) return req.getTotalMinutes();

        Integer h = req.getHours();
        Integer m = req.getMinutes();
        if (h == null && m == null) {
            throw new IllegalArgumentException("totalMinutes 또는 hours/minutes 중 하나는 제공해야 합니다.");
        }
        int hh = (h == null ? 0 : h);
        int mm = (m == null ? 0 : m);
        if (hh < 0 || mm < 0) throw new IllegalArgumentException("음수는 허용되지 않습니다.");
        return hh * 60 + mm;
    }

    private FocusDailyResponse toDailyResponse(FocusDaily e) {
        int mins = e.getMinutes();
        return new FocusDailyResponse(
                e.getLocalDate().toString(),
                mins,
                mins / 60,
                mins % 60,
                e.getZoneId()
        );
    }
}
