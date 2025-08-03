package project.deepdot.schedule.api.dto;

import lombok.Builder;
import lombok.Getter;
import project.deepdot.schedule.domain.Schedule;
import project.deepdot.schedule.domain.scheduleEnum.AlarmStatus;
import project.deepdot.schedule.domain.scheduleEnum.ScheduleImage;
import project.deepdot.schedule.domain.scheduleEnum.ScheduleType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Builder
public class ScheduleResponse {
    private Long scheduleId;
    private String title;
    private LocalTime time;
    private LocalDate calendarDate;
    private ScheduleType type;
    private AlarmStatus alarm;
    private String location;
    private String memo;
    private ScheduleImage image;
    private LocalDateTime modifiedDate;

    public static ScheduleResponse of(Schedule schedule) {
        return ScheduleResponse.builder()
                .scheduleId(schedule.getScheduleId())
                .title(schedule.getTitle())
                .time(schedule.getTime())
                .calendarDate(schedule.getCalendarDate())
                .type(schedule.getType())
                .alarm(schedule.getAlarm())
                .location(schedule.getLocation())
                .memo(schedule.getMemo())
                .image(schedule.getImage())
                .modifiedDate(schedule.getModifiedDate())
                .build();
    }
}