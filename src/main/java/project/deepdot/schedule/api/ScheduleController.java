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
import project.deepdot.user.domain.repository.UserRepository;

import java.security.Principal;
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
        Long id = scheduleService.create(request, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(id);
    }

    // 일정 단일 조회
    @GetMapping("/{scheduleId}")
    public ResponseEntity<ScheduleResponse> findById(@PathVariable Long scheduleId) {
        return ResponseEntity.ok(scheduleService.findById(scheduleId));
    }

    // 사용자별 전체 일정 조회
    @GetMapping
    public ResponseEntity<List<ScheduleResponse>> findAllByUser(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(scheduleService.findAllByUser(user));
    }

    // 일정 수정
    @PutMapping("/{scheduleId}")
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