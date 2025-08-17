package project.deepdot.mainpage.api.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FocusDailyResponse {
    private String localDate;  // yyyy-MM-dd
    private int minutes;       // 총 분
    private int hours;         // 보기 편한 시/분 분해
    private int minutesPart;
    private String zoneId;
}

