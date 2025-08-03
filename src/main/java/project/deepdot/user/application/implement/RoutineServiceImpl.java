package project.deepdot.user.application.implement;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.deepdot.user.api.dto.request.routine.RoutineRequestDto;
import project.deepdot.user.api.dto.request.routine.TaskRequestDto;
import project.deepdot.user.api.dto.response.routine.RoutineResponseDto;
import project.deepdot.user.api.dto.response.routine.TaskResponseDto;
import project.deepdot.user.application.RoutineService;
import project.deepdot.user.domain.Routine;
import project.deepdot.user.domain.Task;
import project.deepdot.user.domain.User;
import project.deepdot.user.domain.repository.RoutineRepository;
import project.deepdot.user.domain.repository.TaskRepository;
import project.deepdot.user.domain.repository.UserRepository;

import java.util.List;


@Service
@RequiredArgsConstructor
public class RoutineServiceImpl implements RoutineService {

    private final RoutineRepository routineRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Transactional
    public RoutineResponseDto createRoutine(Long userId, RoutineRequestDto dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Routine routine = new Routine();
        routine.setUser(user);
        routine.setName(dto.getName());
        routine.setStartTime(dto.getStartTime());
        routine.setAlarm(dto.getAlarm());
        routine.setMon(dto.getMon());
        routine.setTue(dto.getTue());
        routine.setWed(dto.getWed());
        routine.setThu(dto.getThu());
        routine.setFri(dto.getFri());
        routine.setSat(dto.getSat());
        routine.setSun(dto.getSun());

        routineRepository.save(routine);

        for (TaskRequestDto t : dto.getTasks()) {
            Task task = new Task();
            task.setRoutine(routine);
            task.setName(t.getName());
            taskRepository.save(task);
        }

        // routineId 포함한 성공 응답 반환
        return RoutineResponseDto.success(routine.getRoutineId());
    }

    @Override
    public RoutineResponseDto updateRoutine(Long routineId, RoutineRequestDto dto) {
        return null;
    }

    @Override
    public void deleteRoutine(Long routineId) {

    }

    @Override
    public List<TaskResponseDto> getAllTasks(Long userId) {
        return List.of();
    }
}

