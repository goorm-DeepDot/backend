package project.deepdot.mainpage.api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FocusDailyUpsertRequest {
    /** 사용자의 ‘로컬 날짜’ (yyyy-MM-dd) */
    @NotBlank
    private String localDate;

    /** 총 분(권장) — 혹은 hours/minutes 쌍으로 보내도 됨 */
    private Integer totalMinutes;
    private Integer hours;
    private Integer minutes;

    /** 보고 시점의 사용자 타임존(기본 Asia/Seoul) */
    private String zoneId;
}