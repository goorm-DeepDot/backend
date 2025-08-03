package project.deepdot.medication.medicationtime.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.deepdot.medication.medicationtime.application.MedicationTimeService;

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

    // 복용시간 전체 d조회
    @GetMapping("/{medicationId}/times")
    public ResponseEntity<List<MedicationTimeResponse>> getTimes(@PathVariable("medicationId") Long medicationId) {
        return ResponseEntity.ok(timeService.getTimes(medicationId));
    }

    // 복용시간 삭제
    @DeleteMapping("/times/{timeId}")
    public ResponseEntity<Void> deleteTime(@PathVariable("timeId") Long timeId) {
        timeService.deleteTime(timeId);
        return ResponseEntity.noContent().build();
    }
}
