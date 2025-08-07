package project.deepdot.routine.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import project.deepdot.routine.api.dto.request.routine.RoutineRequestDto;
import project.deepdot.routine.api.dto.response.routine.BaseResponse;
import project.deepdot.routine.api.dto.response.routine.RoutineResponseDto;
import project.deepdot.routine.api.dto.response.routine.TaskResponseDto;
import project.deepdot.routine.application.RoutineService;
import project.deepdot.user.user.domain.repository.UserRepository;

import java.util.List;

@RestController
@RequestMapping("/api/routine")
@RequiredArgsConstructor
@Slf4j
public class RoutineController {

    private final RoutineService routineService;
    private final UserRepository userRepository;


    @PostMapping
    public ResponseEntity<RoutineResponseDto> createRoutine(
            Authentication authentication,
            @RequestBody RoutineRequestDto dto
    ) {

        log.info(authentication.getName());

        String username = authentication.getName();

        Long userId = userRepository.findByUsername(username).getUserId();

        return ResponseEntity.ok(routineService.createRoutine(userId, dto));
    }



    @GetMapping("/tasks")
    public ResponseEntity<BaseResponse<List<TaskResponseDto>>> getAllTasks(
            Authentication authentication
            ) {

        String username = authentication.getName();
        Long userId = userRepository.findByUsername(username).getUserId();
        List<TaskResponseDto> tasks = routineService.getAllTasks(userId);
        return ResponseEntity.ok(BaseResponse.success(tasks));

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

