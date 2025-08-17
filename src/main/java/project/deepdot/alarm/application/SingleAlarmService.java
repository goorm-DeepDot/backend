package project.deepdot.alarm.application;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.deepdot.alarm.api.dto.SingleAlarmResponse;
import project.deepdot.alarm.api.dto.SingleAlarmUpsertRequest;
import project.deepdot.alarm.domain.SingleAlarm;
import project.deepdot.alarm.domain.SingleAlarmRepository;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SingleAlarmService {
    private final SingleAlarmRepository repo;
    private final SecureRandom random = new SecureRandom();

    private int newNotificationId(Long userId) {
        // 충돌 회피: 랜덤 생성 → 중복 있으면 재시도
        while (true) {
            int id = 100000 + random.nextInt(900000000); // 양의 int
            boolean exists = repo.findByUserIdAndNotificationId(userId, id).isPresent();
            if (!exists) return id;
        }
    }

    @Transactional
    public SingleAlarmResponse upsert(Long userId, SingleAlarmUpsertRequest req) {
        String zone = (req.getZoneId() == null || req.getZoneId().isBlank()) ? "Asia/Seoul" : req.getZoneId();
        Instant at = Instant.parse(req.getScheduledTimeIso()); // 반드시 UTC "…Z"

          //Integer notifId = req.getNotificationId();
          //if (notifId == null) notifId = newNotificationId(userId);

        //한 번만 대입해서 effectively final 보장
        final int notifId = (req.getNotificationId() != null)
                ? req.getNotificationId().intValue()
                : newNotificationId(userId);


        SingleAlarm entity = repo.findByUserIdAndNotificationId(userId, notifId)
                .map(a -> { a.update(at, req.getTitle(), req.getBody(), req.getActive(), zone); return a; })
                .orElseGet(() -> repo.save(SingleAlarm.builder()
                        .userId(userId)
                        .notificationId(notifId)
                        .scheduledAt(at)
                        .title(req.getTitle())
                        .body(req.getBody())
                        .active(req.getActive())
                        .zoneId(zone)
                        .build()));

        return new SingleAlarmResponse(
                entity.getNotificationId(),
                entity.getScheduledAt().toString(),
                entity.getTitle(),
                entity.getBody(),
                entity.isActive(),
                entity.getZoneId()
        );
    }

    @Transactional(readOnly = true)
    public List<SingleAlarmResponse> list(Long userId) {
        return repo.findByUserId(userId).stream().map(a ->
                new SingleAlarmResponse(
                        a.getNotificationId(),
                        a.getScheduledAt().toString(),
                        a.getTitle(),
                        a.getBody(),
                        a.isActive(),
                        a.getZoneId()
                )
        ).collect(Collectors.toList());
    }

    @Transactional
    public void delete(Long userId, Integer notificationId) {
        SingleAlarm a = repo.findByUserIdAndNotificationId(userId, notificationId)
                .orElseThrow(() -> new IllegalArgumentException("알람이 없습니다"));
        repo.delete(a);
    }
}
