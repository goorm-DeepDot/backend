package project.deepdot.schedule.api.dto;

import lombok.Builder;
import lombok.Getter;
import project.deepdot.schedule.domain.Schedule;
import project.deepdot.schedule.domain.scheduleEnum.ScheduleType;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Builder
public class ScheduleResponse {
    private Long scheduleId;
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

    public static ScheduleResponse of(Schedule schedule) {
        return ScheduleResponse.builder()
                .scheduleId(schedule.getScheduleId())
                .title(schedule.getTitle())
                .time(schedule.getTime())
                .calendarDate(schedule.getCalendarDate())
                .type(schedule.getType())
                .location(schedule.getLocation())
                .memo(schedule.getMemo())
                .image(schedule.getImage())
                .alarm30Before(schedule.isAlarm30Before())
                .alarm60Before(schedule.isAlarm60Before())
                .alarm120Before(schedule.isAlarm120Before())
                .build();
    }
}