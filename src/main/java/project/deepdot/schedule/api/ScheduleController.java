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
import project.deepdot.user.domain.UserPrincipal;

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
                                       @AuthenticationPrincipal UserPrincipal principal) { // ✅ 변경
        User user = principal.getUser(); // ✅ 핵심
        Long scheduleId = scheduleService.create(request, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(scheduleId);
    }

    // 단일 조회
    @GetMapping("/{scheduleId}")
    public ResponseEntity<ScheduleResponse> findById(@PathVariable("scheduleId") Long scheduleId) {
        return ResponseEntity.ok(scheduleService.findById(scheduleId));
    }

    // 특정 날짜의 일정 전체 조회
    @GetMapping("/date")
    public ResponseEntity<List<ScheduleResponse>> findByDate(@RequestParam("date") LocalDate date,
                                                             @AuthenticationPrincipal UserPrincipal principal) { // ✅ 변경
        return ResponseEntity.ok(scheduleService.findByDate(principal.getUser(), date)); // ✅
    }

    // 사용자 전체 일정 조회
    @GetMapping
    public ResponseEntity<List<ScheduleResponse>> findAllByUser(@AuthenticationPrincipal UserPrincipal principal) { // ✅ 변경
        return ResponseEntity.ok(scheduleService.findAllByUser(principal.getUser())); // ✅
    }

    // 수정
    @PatchMapping("/{scheduleId}")
    public ResponseEntity<Void> update(@PathVariable("scheduleId") Long scheduleId,
                                       @RequestBody ScheduleRequest request,
                                       @AuthenticationPrincipal UserPrincipal principal) {
        scheduleService.update(scheduleId, request, principal.getUser());
        return ResponseEntity.noContent().build();
    }

    // 삭제
    @DeleteMapping("/{scheduleId}")
    public ResponseEntity<Void> delete(@PathVariable("scheduleId") Long scheduleId,
                                       @AuthenticationPrincipal UserPrincipal principal) {
        scheduleService.delete(scheduleId, principal.getUser());
        return ResponseEntity.noContent().build();
    }
}