package project.deepdot.alarm.api;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import project.deepdot.alarm.api.dto.response.MedicationAlarmResponse;
import project.deepdot.alarm.api.dto.request.MedicationAlarmUpsertRequest;
import project.deepdot.alarm.application.MedicationAlarmService;
import project.deepdot.user.domain.User;
import project.deepdot.user.domain.UserPrincipal;

import java.util.List;

@RestController
@RequestMapping("/api/alarms/medication")
@RequiredArgsConstructor
public class MedicationAlarmController {

    private final MedicationAlarmService service;

    /**
     * 약 이름 기준 업서트 (생성/갱신)
     * body: { medicationName, active, times:["08:00","12:00","18:00"] }
     */
    @PostMapping
    public ResponseEntity<MedicationAlarmResponse> upsert(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody MedicationAlarmUpsertRequest req) {
        User user = principal.getUser();
        return ResponseEntity.ok(service.upsert(user.getUserId(), req));
    }

    /**
     * 내 알람 목록
     */
    @GetMapping
    public ResponseEntity<List<MedicationAlarmResponse>> list(
            @AuthenticationPrincipal UserPrincipal principal) {
        User user = principal.getUser();
        return ResponseEntity.ok(service.list(user.getUserId()));
    }

    /**
     * 알람 삭제
     */
    @DeleteMapping("/{alarmId}")
    public ResponseEntity<Void> delete(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long alarmId) {
        User user = principal.getUser();
        service.delete(user.getUserId(), alarmId);
        return ResponseEntity.noContent().build();
    }
}
