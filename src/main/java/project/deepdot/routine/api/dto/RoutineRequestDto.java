package project.deepdot.routine.api.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
public class RoutineRequestDto {

    private String name;

    private Long goalId;        // 기존 목표 선택
    private String newGoalName; // 새 목표 추가

    private Boolean mon;
    private Boolean tue;
    private Boolean wed;
    private Boolean thu;
    private Boolean fri;
    private Boolean sat;
    private Boolean sun;

    // [MODIFIED] alarm → active
    // @JsonAlias("alarm") // (선택) 예전 클라가 alarm을 보내도 매핑하려면 주석 해제
    private Boolean active;

    @JsonProperty("start_time")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime startTime;

    private String memo;        // [NEW]
}