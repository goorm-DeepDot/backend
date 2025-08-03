package project.deepdot.schedule.api.dto;

import lombok.Getter;
import project.deepdot.schedule.domain.scheduleEnum.AlarmStatus;
import project.deepdot.schedule.domain.scheduleEnum.ScheduleImage;
import project.deepdot.schedule.domain.scheduleEnum.ScheduleType;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
public class ScheduleRequest {
    private String title;
    private LocalTime time;
    private LocalDate calendarDate;
    private ScheduleType type;
    private AlarmStatus alarm;
    private String location;
    private String memo;
    private ScheduleImage image;
}
