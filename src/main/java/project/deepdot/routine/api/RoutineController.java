package project.deepdot.routine.api;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import project.deepdot.routine.api.dto.*;
import project.deepdot.routine.application.RoutineService;
import project.deepdot.user.domain.User;
import project.deepdot.user.domain.UserPrincipal;
import project.deepdot.user.domain.repository.UserRepository;

import java.util.List;

@RestController
@RequestMapping("/api/routine")
@RequiredArgsConstructor
@Slf4j
public class RoutineController {

    private final RoutineService routineService;

    @PostMapping
    public ResponseEntity<RoutineResponseDto> createRoutine(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestBody RoutineRequestDto dto
    ) {
        User user = principal.getUser();
        Long userId = user.getUserId();
        return ResponseEntity.ok(routineService.createRoutine(userId, dto));
    }

    @PatchMapping("/{routineId}")
    public ResponseEntity<RoutineResponseDto> updateRoutine(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long routineId,
            @RequestBody RoutineRequestDto dto
    ) {
        // principal는 여기선 권한 확인 용도로만 필요(서비스는 routineId로 엔티티를 조회/수정)
        return ResponseEntity.ok(routineService.updateRoutine(routineId, dto));
    }

    @DeleteMapping("/{routineId}")
    public ResponseEntity<Void> deleteRoutine(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long routineId
    ) {
        routineService.deleteRoutine(routineId);
        return ResponseEntity.noContent().build();
    }

    // ---------- 목표 관리 ----------
    @GetMapping("/goals")
    public ResponseEntity<BaseResponse<List<GoalResponseDto>>> listGoals(
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        Long userId = principal.getUser().getUserId();
        return ResponseEntity.ok(BaseResponse.success(routineService.listGoals(userId)));
    }

    @PostMapping("/goals")
    public ResponseEntity<BaseResponse<GoalResponseDto>> addGoal(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestBody GoalRequestDto req
    ) {
        Long userId = principal.getUser().getUserId();
        return ResponseEntity.ok(BaseResponse.success(routineService.addGoal(userId, req)));
    }

    @PatchMapping("/goals/{goalId}")
    public ResponseEntity<BaseResponse<GoalResponseDto>> updateGoal(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long goalId,
            @RequestBody GoalRequestDto req
    ) {
        Long userId = principal.getUser().getUserId();
        return ResponseEntity.ok(BaseResponse.success(routineService.updateGoal(userId, goalId, req)));
    }

    @DeleteMapping("/goals/{goalId}")
    public ResponseEntity<Void> deleteGoal(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long goalId
    ) {
        Long userId = principal.getUser().getUserId();
        routineService.deleteGoal(userId, goalId);
        return ResponseEntity.noContent().build();
    }

    // ---------- 목표별 루틴 조회 ----------
    @GetMapping("/goals/{goalId}/routines")
    public ResponseEntity<BaseResponse<List<RoutineReadDto>>> listRoutinesByGoal(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long goalId
    ) {
        Long userId = principal.getUser().getUserId();
        return ResponseEntity.ok(BaseResponse.success(routineService.listRoutinesByGoal(userId, goalId)));
    }
}
