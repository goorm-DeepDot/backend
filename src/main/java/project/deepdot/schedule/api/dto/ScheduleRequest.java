package project.deepdot.schedule.api.dto;

import lombok.Getter;
import project.deepdot.schedule.domain.scheduleEnum.ScheduleType;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
public class ScheduleRequest {
    private String title;
    private LocalTime time;
    private LocalDate calendarDate;
    private ScheduleType type;
    private String location;
    private String memo;
    private String image;
    private boolean alarm30Before;
    private boolean alarm60Before;
    private boolean alarm120Before;
}
