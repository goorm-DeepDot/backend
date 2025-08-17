package project.deepdot.schedule.api.dto;

import lombok.*;
import project.deepdot.schedule.domain.scheduleEnum.ScheduleType;

import java.time.LocalDate;
import java.time.LocalTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED) // JSON 역직렬화용
@AllArgsConstructor                                // Builder 내부적으로 필요
@Builder
@Getter
public class ScheduleRequest {
    private String title;
    private LocalTime time;
    private LocalDate startDate;
    private LocalDate endDate;
    private ScheduleType type;
    private String location;
    private String memo;
    private String image;
    private boolean alarm30Before;
    private boolean alarm60Before;
    private boolean alarm120Before;
    private boolean isRecurring;
}
