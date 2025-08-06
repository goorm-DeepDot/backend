package project.deepdot.schedule.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import project.deepdot.schedule.api.dto.ScheduleRequest;
import project.deepdot.schedule.api.dto.ScheduleResponse;
import project.deepdot.schedule.application.ScheduleService;
import project.deepdot.user.domain.User;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/schedule")
public class ScheduleController {

    private final ScheduleService scheduleService;

    // 일정 추가
    @PostMapping
    public ResponseEntity<Long> create(@RequestBody ScheduleRequest request,
                                       @AuthenticationPrincipal User user) {
        Long scheduleId = scheduleService.create(request, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(scheduleId);
    }

    // 일정 단일 조회
    @GetMapping("/{scheduleId}")
    public ResponseEntity<ScheduleResponse> findById(@PathVariable Long scheduleId) {
        return ResponseEntity.ok(scheduleService.findById(scheduleId));
    }

    // 특정 날짜의 일정 전체 조회 (요일별 표시 가능)
    // 예: /api/schedule/date?date=2025-08-08
    @GetMapping("/date")
    public ResponseEntity<List<ScheduleResponse>> findByDate(@RequestParam("date") LocalDate date,
                                                             @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(scheduleService.findByDate(user, date));
    }

    // 사용자 전체 일정 조회
    @GetMapping
    public ResponseEntity<List<ScheduleResponse>> findAllByUser(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(scheduleService.findAllByUser(user));
    }

    // 일정 수정
    @PatchMapping("/{scheduleId}")
    public ResponseEntity<Void> update(@PathVariable Long scheduleId,
                                       @RequestBody ScheduleRequest request,
                                       @AuthenticationPrincipal User user) {
        scheduleService.update(scheduleId, request, user);
        return ResponseEntity.noContent().build();
    }

    // 일정 삭제
    @DeleteMapping("/{scheduleId}")
    public ResponseEntity<Void> delete(@PathVariable Long scheduleId,
                                       @AuthenticationPrincipal User user) {
        scheduleService.delete(scheduleId, user);
        return ResponseEntity.noContent().build();
    }
}