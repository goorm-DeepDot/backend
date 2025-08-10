package project.deepdot.medication.medicationtime.domain;

import jakarta.persistence.*;
import lombok.*;
import project.deepdot.medication.medication.domain.Medication;

import java.time.LocalTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "medication_time")
@Entity
public class MedicationTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //한 id에 최대 3개의 먹는 시간 작성 가능.
    @Column(name = "time", nullable = false)
    private LocalTime time;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medication_id", nullable = false)
    private Medication medication;

    @Builder
    public MedicationTime(LocalTime time, Medication medication) {
        this.time = time;
        this.medication = medication;
    }
}