package project.deepdot.mainpage.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class FocusWeekResponse {
    private String weekStart;    // yyyy-MM-dd (월요일)
    private String weekEnd;      // yyyy-MM-dd (일요일)
    private int totalMinutes;    // 주간 합계
    private int totalHours;
    private int totalMinutesPart;
    private List<Day> days;      // 월~일 7칸

    @Getter @AllArgsConstructor
    public static class Day {
        private String date;     // yyyy-MM-dd
        private int weekday;     // 1=월 ... 7=일
        private int minutes;
        private int hours;
        private int minutesPart;
    }
}