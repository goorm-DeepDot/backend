package project.deepdot.user.application;

import project.deepdot.user.api.dto.request.routine.RoutineRequestDto;
import project.deepdot.user.api.dto.response.routine.RoutineResponseDto;
import project.deepdot.user.api.dto.response.routine.TaskResponseDto;

import java.util.List;

public interface RoutineService {
    RoutineResponseDto createRoutine(Long userId, RoutineRequestDto dto);
    RoutineResponseDto updateRoutine(Long routineId, RoutineRequestDto dto);
    void deleteRoutine(Long routineId);
    List<TaskResponseDto> getAllTasks(Long userId);
}
