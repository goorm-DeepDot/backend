package project.deepdot.medication.medicationtime.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import project.deepdot.medication.medicationtime.application.MedicationTimeService;
import project.deepdot.user.domain.UserPrincipal;

import java.time.LocalTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/medication")
public class MedicationTimeController {

    private final MedicationTimeService timeService;

    // 복용시간 추가
    @PostMapping("/{medicationId}/times")
    public ResponseEntity<Void> addTime(
            @PathVariable("medicationId") Long medicationId,
            @RequestBody MedicationTimeRequest request) {
        timeService.addTime(medicationId, request);
        return ResponseEntity.status(201).build();
    }

//    // 복용시간 전체 조회
//    @GetMapping("/{medicationId}/times")
//    public ResponseEntity<List<LocalTime>> getTimes(
//            @PathVariable Long medicationId,
//            @AuthenticationPrincipal UserPrincipal userPrincipal) {
//        return ResponseEntity.ok(
//                timeService.getTimes(medicationId, userPrincipal.getUser())
//        );
//    }


    // 복용시간 삭제
    @DeleteMapping("/times/{timeId}")
    public ResponseEntity<Void> deleteTime(@PathVariable("timeId") Long timeId) {
        timeService.deleteTime(timeId);
        return ResponseEntity.noContent().build();
    }
}
