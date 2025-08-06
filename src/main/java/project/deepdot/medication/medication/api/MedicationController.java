package project.deepdot.medication.medication.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import project.deepdot.medication.medication.application.MedicationService;
import project.deepdot.user.domain.User;
import project.deepdot.user.domain.UserPrincipal;
import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/medication")
public class MedicationController {

    private final MedicationService medicationService;

    // 등록 (현재 로그인한 사용자 기준)
    @PostMapping
    public ResponseEntity<Long> create(@RequestBody MedicationRequest request,
                                       @AuthenticationPrincipal UserPrincipal userPrincipal) {
        User user = userPrincipal.getUser();
        Long id = medicationService.create(request, user);
        return ResponseEntity.created(URI.create("/api/medications/" + id)).body(id); // 201 Created
    }

    // 전체 조회 (현재 로그인한 사용자 기준)
    @GetMapping("/all")
    public ResponseEntity<List<MedicationResponse>> getAll(
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        return ResponseEntity.ok(
                medicationService.findAllByUser(userPrincipal.getUser().getUserId())
        );
    }

    // 단일 조회
    @GetMapping("/{medicationId}")
    public ResponseEntity<MedicationResponse> findById(@PathVariable("medicationId") Long id,
                                                       @AuthenticationPrincipal UserPrincipal userPrincipal) {
        return ResponseEntity.ok(
                medicationService.findById(id, userPrincipal.getUser())
        );
    }

    // 수정
    @PatchMapping("/{medicationId}")
    public ResponseEntity<Void> update(@PathVariable("medicationId") Long id,
                                       @RequestBody MedicationRequest request,
                                       @AuthenticationPrincipal UserPrincipal userPrincipal) {
        medicationService.update(id, request, userPrincipal.getUser());
        return ResponseEntity.noContent().build();
    }

    // 삭제
    @DeleteMapping("/{medicationId}")
    public ResponseEntity<Void> delete(@PathVariable("medicationId") Long id,
                                       @AuthenticationPrincipal UserPrincipal userPrincipal) {
        medicationService.delete(id, userPrincipal.getUser());
        return ResponseEntity.noContent().build();
    }
}