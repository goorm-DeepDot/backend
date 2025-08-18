package project.deepdot.routine.domain;

import jakarta.persistence.*;
import lombok.*;
import project.deepdot.user.domain.User;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "routine")
public class Routine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "routine_id")
    private Long routineId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "name", length = 20, nullable = false)
    private String name;

    // [MODIFIED] alarm → active
    @Column(name = "active")
    private Boolean active;

    @Column(name = "start_time", nullable = false)
    @com.fasterxml.jackson.annotation.JsonFormat(pattern = "HH:mm")
    private java.time.LocalTime startTime;

    private Boolean mon;
    private Boolean tue;
    private Boolean wed;
    private Boolean thu;
    private Boolean fri;
    private Boolean sat;
    private Boolean sun;

    // [NEW]
    @Column(name = "memo", length = 500)
    private String memo;

    // [MODIFIED] Goal 1 : N Routine
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "goal_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_routine_goal")
    )
    private Goal goal;
}