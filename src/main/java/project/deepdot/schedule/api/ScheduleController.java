package project.deepdot.schedule.api;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
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
                                       @AuthenticationPrincipal UserPrincipal principal) {
        User user = principal.getUser(); // 핵심
        Long scheduleId = scheduleService.create(request, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(scheduleId);
    }

    // 단일 조회
    @GetMapping("/{scheduleId}")
    public ResponseEntity<ScheduleResponse> findById(@PathVariable("scheduleId") Long scheduleId) {
        return ResponseEntity.ok(scheduleService.findById(scheduleId));
    }

//    특정 날짜에 '해야 하는' 전체 일정
//    (겹치는 일정 포함: startDate ≤ date ≤ endDate)
    @GetMapping("/date")
    public ResponseEntity<List<ScheduleResponse>> findByDate(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        return ResponseEntity.ok(scheduleService.findByDate(principal.getUser(), date));
    }

    //기간 [from, to] 과 겹치는 모든 일정
    //(startDate ≤ to AND endDate ≥ from)
    @GetMapping("/range")
    public ResponseEntity<List<ScheduleResponse>> findInRange(
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam("to")   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        return ResponseEntity.ok(scheduleService.findInRange(principal.getUser(), from, to));
    }

    // 각 날짜마다 하루짜리 일정을 여러 건 생성.
    @PostMapping("/batch")
    public ResponseEntity<List<Long>> createBatch(
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam("to")   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @RequestBody ScheduleRequest template,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        List<Long> ids = scheduleService.createBatch(template, principal.getUser(), from, to);
        return ResponseEntity.status(HttpStatus.CREATED).body(ids);
    }

    // 사용자 전체 일정 조회
    @GetMapping
    public ResponseEntity<List<ScheduleResponse>> findAllByUser(@AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(scheduleService.findAllByUser(principal.getUser()));
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