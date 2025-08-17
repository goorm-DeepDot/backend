package project.deepdot.schedule.domain;

import jakarta.persistence.*;
import lombok.*;
import project.deepdot.schedule.domain.scheduleEnum.ScheduleType;
import project.deepdot.user.domain.User;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "schedule")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="schedule_id")
    private Long scheduleId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, length = 100)
    private LocalTime time;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ScheduleType type;

    @Column(columnDefinition = "TEXT")
    private String location;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(columnDefinition = "TEXT")
    private String memo;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String image;

    // Boolean 컬럼으로 교체
    @Column(name = "alarm_30_before", nullable = false)
    private boolean alarm30Before;

    @Column(name = "alarm_60_before", nullable = false)
    private boolean alarm60Before;

    @Column(name = "alarm_120_before", nullable = false)
    private boolean alarm120Before;

    @Column(nullable = false)
    private boolean isRecurring;

    @Builder
    public Schedule(User user, String title, LocalTime time, LocalDate startDate,
                    LocalDate endDate, ScheduleType type, String location, String memo,
                    String image, boolean alarm30Before, boolean alarm60Before, boolean alarm120Before, boolean isRecurring) {
        this.user = user;
        this.title = title;
        this.time = time;
        this.startDate = startDate;
        this.endDate = endDate;
        this.type = type;
        this.location = location;
        this.memo = memo;
        this.image = image;
        this.alarm30Before = alarm30Before;
        this.alarm60Before = alarm60Before;
        this.alarm120Before = alarm120Before;
        this.isRecurring = isRecurring;
    }

    //수정
    public void update(String title, LocalTime time, LocalDate startDate, LocalDate endDate,
                       ScheduleType type, String location,
                       String memo, String image,
                       boolean alarm30Before, boolean alarm60Before, boolean alarm120Before, boolean isRecurring) {
        this.title = title;
        this.time = time;
        this.startDate = startDate;
        this.endDate = endDate;
        this.type = type;
        this.location = location;
        this.memo = memo;
        this.image = image;
        this.alarm30Before = alarm30Before;
        this.alarm60Before = alarm60Before;
        this.alarm120Before = alarm120Before;
        this.isRecurring = isRecurring;
    }
}