package project.deepdot.setting.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "alarm_settings",
        uniqueConstraints = @UniqueConstraint(name = "uk_alarm_settings_user", columnNames = "user_id"))
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class AlarmSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="user_id", nullable=false)
    private Long userId;

    @Column(name="routine_enabled", nullable=false)
    private boolean routineEnabled;

    @Column(name="task_enabled", nullable=false)
    private boolean taskEnabled;

    @Column(name="medication_enabled", nullable=false)
    private boolean medicationEnabled;

    @Column(name="updated_at", nullable=false)
    private Instant updatedAt;

    /** null 값은 변경하지 않음 */
    // [NEW]
    public void apply(Boolean routine, Boolean task, Boolean medication) {
        if (routine != null) this.routineEnabled = routine;
        if (task != null) this.taskEnabled = task;
        if (medication != null) this.medicationEnabled = medication;
        this.updatedAt = Instant.now();
    }
}