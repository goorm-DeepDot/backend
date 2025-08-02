package project.deepdot.medication.medication.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.deepdot.medication.medication.application.MedicationService;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/medication")
public class MedicationController {

    private final MedicationService medicationService;

    // 등록
    @PostMapping
    public ResponseEntity<Long> create(@RequestBody MedicationRequest request) {
        Long id = medicationService.create(request);
        return ResponseEntity.created(URI.create("/api/medications/" + id )).body(id); // 201 Created
    }

    // 전체 조회
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<MedicationResponse>> getByUser(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok(medicationService.findAllByUser(userId));
    }

    // 단일 조회
    @GetMapping("/{medicationId}")
    public ResponseEntity<MedicationResponse> findById(@PathVariable("medicationId") Long id) {
        return ResponseEntity.ok(medicationService.findById(id));
    }

    // 수정
    @PutMapping("/{medicationId}")
    public ResponseEntity<Void> update(@PathVariable("medicationId")Long id, @RequestBody MedicationRequest request) {
        medicationService.update(id, request);
        return ResponseEntity.noContent().build(); // 204 No Content
    }

    // 삭제
    @DeleteMapping("/{medicationId}")
    public ResponseEntity<Void> delete(@PathVariable("medicationId") Long id) {
        medicationService.delete(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}