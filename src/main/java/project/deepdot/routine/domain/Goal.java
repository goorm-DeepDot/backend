package project.deepdot.routine.domain;

import jakarta.persistence.*;
import lombok.*;
import project.deepdot.user.domain.User;

@Entity
@Table(
        name = "goal",
        uniqueConstraints = @UniqueConstraint(name = "uk_goal_user_name", columnNames = {"user_id", "name"})
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Goal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "goal_id")
    private Long goalId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_goal_user"))
    private User user;

    @Column(name = "name", length = 30, nullable = false)
    private String name;
}
