package project.deepdot.user.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "USER")
public class User{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID")
    private Long userId;

    @Column(name = "EMAIL", length = 50, nullable = false, unique = true)
    private String email;

    @Column(name = "USER_NAME", length = 20, nullable = false)
    private String name;
}