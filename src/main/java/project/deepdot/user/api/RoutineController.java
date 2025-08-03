package project.deepdot.user.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import project.deepdot.user.api.dto.request.routine.RoutineRequestDto;
import project.deepdot.user.api.dto.response.routine.RoutineResponseDto;
import project.deepdot.user.api.dto.response.routine.TaskResponseDto;
import project.deepdot.user.application.RoutineService;


import java.util.List;

@RestController
@RequestMapping("/api/routine")
@RequiredArgsConstructor
public class RoutineController {

    private final RoutineService routineService;

    @PostMapping
    public ResponseEntity<RoutineResponseDto> createRoutine(
            @AuthenticationPrincipal org.springframework.security.core.userdetails.User user, // JWT에서 꺼낸 사용자 정보
            //@RequestParam Long userId,
            @RequestBody RoutineRequestDto dto
    ) {
        // JWT 토큰 subject = userId 로 설정되어 있으므로
        Long userId = Long.parseLong(user.getUsername());

        return ResponseEntity.ok(routineService.createRoutine(userId, dto));
    }

    @GetMapping("/tasks")
    public ResponseEntity<List<TaskResponseDto>> getAllTasks(
            @RequestParam Long userId
    ) {
        return ResponseEntity.ok(routineService.getAllTasks(userId));
    }

    @PatchMapping("/{routineId}")
    public ResponseEntity<RoutineResponseDto> updateRoutine(
            @PathVariable Long routineId,
            @RequestBody RoutineRequestDto dto
    ) {
        return ResponseEntity.ok(routineService.updateRoutine(routineId, dto));
    }

    @DeleteMapping("/{routineId}")
    public ResponseEntity<Void> deleteRoutine(@PathVariable Long routineId) {
        routineService.deleteRoutine(routineId);
        return ResponseEntity.noContent().build();
    }
}

