package project.deepdot.schedule.domain;

import jakarta.persistence.*;
import lombok.*;
import project.deepdot.schedule.domain.scheduleEnum.AlarmStatus;
import project.deepdot.schedule.domain.scheduleEnum.ScheduleImage;
import project.deepdot.schedule.domain.scheduleEnum.ScheduleType;
import project.deepdot.user.domain.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

    @Column(name = "calendar_date", nullable = false)
    private LocalDate calendarDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ScheduleType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AlarmStatus alarm;

    @Column(columnDefinition = "TEXT")
    private String location;

    @Column(columnDefinition = "TEXT")
    private String memo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ScheduleImage image;

    @Column(name = "modified_date", nullable = false)
    private LocalDateTime modifiedDate;

    @Builder
    public Schedule(User user, String title, LocalTime time, LocalDate calendarDate,
                    ScheduleType type, AlarmStatus alarm, String location, String memo,
                    ScheduleImage image, LocalDateTime modifiedDate) {
        this.user = user;
        this.title = title;
        this.time = time;
        this.calendarDate = calendarDate;
        this.type = type;
        this.alarm = alarm;
        this.location = location;
        this.memo = memo;
        this.image = image;
        this.modifiedDate = modifiedDate;
    }

    //수정
    public void update(String title, LocalTime time, LocalDate calendarDate,
                       ScheduleType type, AlarmStatus alarm, String location,
                       String memo, ScheduleImage image) {
        this.title = title;
        this.time = time;
        this.calendarDate = calendarDate;
        this.type = type;
        this.alarm = alarm;
        this.location = location;
        this.memo = memo;
        this.image = image;
        this.modifiedDate = LocalDateTime.now();
    }

}