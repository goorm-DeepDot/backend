package project.deepdot.medication.medication.domain;

import jakarta.persistence.*;
import lombok.*;
import project.deepdot.medication.medicationtime.domain.MedicationTime;
import project.deepdot.user.domain.User;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(
        name = "medication",
        uniqueConstraints = {
                //같은 약 이름을 유저별로만 유일하게 만들기
                @UniqueConstraint(columnNames = {"user_id", "name"})
        }
)
@Entity
public class Medication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "medication_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 약 이름을 db에 저장. -> 검색용도이기 때문.
    @Column(name = "name", nullable = false)
    private String name;

    // 알람 여부
    @Column(name = "alarm", nullable = false)
    private boolean alarm;

    @OneToMany(mappedBy = "medication", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MedicationTime> medicationTimes = new ArrayList<>();

    public List<MedicationTime> getMedicationTimes() {
        return medicationTimes;
    }

    // builder
    @Builder
    public Medication(User user, String name, boolean alarm) {
        this.user=user;
        this.name = name;
        this.alarm = alarm;
    }

    //수정
    public void update(String name, boolean alarm) {
        this.name = name;
        this.alarm = alarm;
    }
}
