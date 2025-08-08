package project.deepdot.user.api.token;

import jakarta.persistence.*;
import lombok.*;
import project.deepdot.user.domain.User;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "USER_REFRESH_TOKEN")
@Entity
public class UserRefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "REFRESH_TOKEN_ID")
    private Long refreshTokenId;

    @Column(name = "REFRESH_TOKEN")
    private String refreshToken;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;
}
