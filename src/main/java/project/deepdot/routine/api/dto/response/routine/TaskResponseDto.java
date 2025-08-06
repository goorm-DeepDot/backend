package project.deepdot.routine.api.dto.response.routine;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import project.deepdot.user.api.dto.response.ResponseDto;
import project.deepdot.routine.domain.Routine;
import project.deepdot.routine.domain.Task;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class TaskResponseDto extends ResponseDto {
    private Long taskId;
    private String taskName;
    private Long routineId;


    private Boolean mon;
    private Boolean tue;
    private Boolean wed;
    private Boolean thu;
    private Boolean fri;
    private Boolean sat;
    private Boolean sun;

    public static TaskResponseDto fromEntity(Task task) {
        Routine r = task.getRoutine();
        return TaskResponseDto.builder()
                .taskId(task.getTaskId())
                .taskName(task.getName())
                .mon(r.getMon())
                .tue(r.getTue())
                .wed(r.getWed())
                .thu(r.getThu())
                .fri(r.getFri())
                .sat(r.getSat())
                .sun(r.getSun())
                .build();
    }
}
