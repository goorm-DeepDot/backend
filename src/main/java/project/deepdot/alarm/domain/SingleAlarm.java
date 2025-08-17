package project.deepdot.alarm.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "single_alarm",
        uniqueConstraints = {
                @UniqueConstraint(name="uk_single_alarm_user_notif", columnNames = {"user_id","notification_id"})
        })
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class SingleAlarm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 내부 PK

    @Column(name="user_id", nullable=false)
    private Long userId;

    // Flutter zonedSchedule에서 쓰는 ID
    @Column(name="notification_id", nullable=false)
    private Integer notificationId;

    // UTC 보관
    @Column(name="scheduled_at", nullable=false)
    private Instant scheduledAt;

    @Column(name="title", length=200)
    private String title;

    @Column(name="body", length=500)
    private String body;

    @Column(name="is_active", nullable=false)
    private boolean active;

    @Column(name="zone_id", nullable=false, length=64)
    private String zoneId; // 예: Asia/Seoul

    public void update(Instant scheduledAt, String title, String body, boolean active, String zoneId) {
        this.scheduledAt = scheduledAt;
        this.title = title;
        this.body = body;
        this.active = active;
        this.zoneId = zoneId;
    }
}
