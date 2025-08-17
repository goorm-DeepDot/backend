package project.deepdot.alarm.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;

@Entity
@Table(name = "weekly_alarm",
        uniqueConstraints = {
                @UniqueConstraint(name="uk_weekly_alarm_user_base", columnNames = {"user_id","base_notification_id"})
        })
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class WeeklyAlarm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 내부 PK

    @Column(name="user_id", nullable=false)
    private Long userId;

    // Flutter 측 요일별 실ID = baseId*10 + weekday
    @Column(name="base_notification_id", nullable=false)
    private Integer baseNotificationId;

    // 로컬 기준 시각(HH:mm) → LocalTime 권장
    @Column(name="time_local", nullable=false)
    private LocalTime timeLocal; // "09:00"

    // 요일 비트마스크 (1=월..7=일 → 0..6 비트)
    @Column(name="weekday_mask", nullable=false)
    private Integer weekdayMask;

    @Column(name="title", length=200)
    private String title;

    @Column(name="body", length=500)
    private String body;

    @Column(name="is_active", nullable=false)
    private boolean active;

    @Column(name="zone_id", nullable=false, length=64)
    private String zoneId; // 예: Asia/Seoul

    public void update(LocalTime timeLocal, int weekdayMask, String title, String body, boolean active, String zoneId) {
        this.timeLocal = timeLocal;
        this.weekdayMask = weekdayMask;
        this.title = title;
        this.body = body;
        this.active = active;
        this.zoneId = zoneId;
    }
}
