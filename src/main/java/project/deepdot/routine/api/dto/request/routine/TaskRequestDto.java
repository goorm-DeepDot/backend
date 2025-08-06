package project.deepdot.routine.api.dto.request.routine;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskRequestDto {
    private Long taskId; // 수정 시 필요, 생성 시 null
    private String name;
}
