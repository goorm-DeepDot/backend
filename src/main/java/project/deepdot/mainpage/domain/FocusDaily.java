package project.deepdot.mainpage.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "focus_daily",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_focus_daily_user_date",
                columnNames = {"user_id", "local_date"}))
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class FocusDaily {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="user_id", nullable=false)
    private Long userId;

    /** 사용자의 로컬 ‘날짜’ (예: 2025-08-16) */
    @Column(name="local_date", nullable=false)
    private LocalDate localDate;

    /** 그 날의 총 집중 시간(분) */
    @Column(name="minutes", nullable=false)
    private Integer minutes;

    /** 사용자가 보고한 로컬 타임존(예: Asia/Seoul) */
    @Column(name="zone_id", nullable=false, length=64)
    private String zoneId;

    public void updateMinutes(int minutes, String zoneId) {
        this.minutes = minutes;
        if (zoneId != null && !zoneId.isBlank()) {
            this.zoneId = zoneId;
        }
    }
}