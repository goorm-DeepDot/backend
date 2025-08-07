package project.deepdot.user.user.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.deepdot.user.user.domain.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    //아이디, 이메일 중복 확인
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    //인증 코드 발송 용도
    Optional<User> findByEmail(String email);

    //조회 기능.
    Optional<User> findByUsername(String username);

    Optional<User> findOneWithAuthoritiesByUsername(String username);
    Optional<User> findByUsernameAndEmail(String username, String email);


}