package project.deepdot.alarm.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;

@Entity
@Table(name="medication_alarm_time") // ← 기존 medication_time 과 구분!
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class MedicationAlarmTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    private LocalTime time; // 예: 08:00

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="medication_alarm_id", nullable=false,
            foreignKey = @ForeignKey(name="fk_med_alarm_time_alarm"))
    private MedicationAlarm medicationAlarm;

    public void setMedicationAlarm(MedicationAlarm alarm) {
        this.medicationAlarm = alarm;
    }
}
