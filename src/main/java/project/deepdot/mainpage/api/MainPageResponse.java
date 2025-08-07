package project.deepdot.mainpage.api;

import lombok.Builder;
import lombok.Getter;
import project.deepdot.schedule.domain.Schedule;

@Getter
@Builder
public class MainPageResponse {
    private Long scheduleId;
    private String title;

    public static MainPageResponse from(Schedule schedule) {
        return MainPageResponse.builder()
                .scheduleId(schedule.getScheduleId())
                .title(schedule.getTitle())
                .build();
    }
}