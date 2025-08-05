package project.deepdot.medication.medication.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.deepdot.medication.medication.application.MedicationService;
import project.deepdot.user.domain.User;
import project.deepdot.user.domain.repository.UserRepository;

import java.net.URI;
import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/medication")
public class MedicationController {

    private final MedicationService medicationService;
    private final UserRepository userRepository;

    // 등록 (현재 로그인한 사용자 기준)
    @PostMapping
    public ResponseEntity<Long> create(@RequestBody MedicationRequest request, Principal principal) {
        User user = getCurrentUser(principal);
        Long id = medicationService.create(request, user);
        return ResponseEntity.created(URI.create("/api/medications/" + id)).body(id); // 201 Created
    }

    // 전체 조회 (현재 로그인한 사용자 기준)
    @GetMapping("/my")
    public ResponseEntity<List<MedicationResponse>> getByCurrentUser(Principal principal) {
        User user = getCurrentUser(principal);
        return ResponseEntity.ok(medicationService.findAllByUser(user.getUserId()));
    }

    // 단일 조회
    @GetMapping("/{medicationId}")
    public ResponseEntity<MedicationResponse> findById(@PathVariable("medicationId") Long id, Principal principal) {
        User user = getCurrentUser(principal);
        return ResponseEntity.ok(medicationService.findById(id, user));
    }

    // 수정
    @PatchMapping("/{medicationId}")
    public ResponseEntity<Void> update(@PathVariable("medicationId") Long id,
                                       @RequestBody MedicationRequest request,
                                       Principal principal) {
        User user = getCurrentUser(principal);
        medicationService.update(id, request, user);
        return ResponseEntity.noContent().build();
    }

    // 삭제
    @DeleteMapping("/{medicationId}")
    public ResponseEntity<Void> delete(@PathVariable("medicationId") Long id, Principal principal) {
        User user = getCurrentUser(principal);
        medicationService.delete(id, user);
        return ResponseEntity.noContent().build();
    }

    // 현재 로그인한 User 엔티티 조회
    private User getCurrentUser(Principal principal) {
        return userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new IllegalArgumentException("로그인한 사용자를 찾을 수 없습니다."));
    }
}