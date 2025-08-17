package project.deepdot.alarm.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "medication_alarm",
        uniqueConstraints = {
                @UniqueConstraint(name="uk_med_alarm_user_name", columnNames = {"user_id","medication_name"})
        })
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class MedicationAlarm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="user_id", nullable=false)
    private Long userId;

    @Column(name="medication_name", nullable=false, length=100)
    private String medicationName;

    @Column(name="is_active", nullable=false)
    private boolean active;

    @OneToMany(mappedBy = "medicationAlarm",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @Builder.Default
    private List<MedicationAlarmTime> times = new ArrayList<>();

    /** 약 이름/활성여부/시각(0~3개)을 통째로 갱신 */
    public void update(String medicationName, boolean active, List<MedicationAlarmTime> newTimes) {
        this.medicationName = medicationName;
        this.active = active;

        if (this.times == null) this.times = new ArrayList<>();
        this.times.clear();

        if (newTimes != null) {
            for (MedicationAlarmTime t : new ArrayList<>(newTimes)) { // 방어적 복사
                t.setMedicationAlarm(this);
                this.times.add(t);
            }
        }
    }
}
