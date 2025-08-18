package project.deepdot.routine.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoutineReadDto {

    private Long routineId;
    private String name;

    private Long goalId;
    private String goalName;

    private Boolean active;

    @JsonProperty("start_time")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime startTime;

    // 요일
    private Boolean mon;
    private Boolean tue;
    private Boolean wed;
    private Boolean thu;
    private Boolean fri;
    private Boolean sat;
    private Boolean sun;

    private String memo;

    public static RoutineReadDto of(
            Long routineId, String name, Long goalId, String goalName, Boolean active,
            LocalTime startTime, Boolean mon, Boolean tue, Boolean wed, Boolean thu,
            Boolean fri, Boolean sat, Boolean sun, String memo
    ) {
        return RoutineReadDto.builder()
                .routineId(routineId)
                .name(name)
                .goalId(goalId)
                .goalName(goalName)
                .active(active)
                .startTime(startTime)
                .mon(mon).tue(tue).wed(wed).thu(thu).fri(fri).sat(sat).sun(sun)
                .memo(memo)
                .build();
    }
}
