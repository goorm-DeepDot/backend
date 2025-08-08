package project.deepdot.user.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.deepdot.user.domain.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // 아이디, 이메일 찾기
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);

    //비밀번호 찾기
    Optional<User> findByUsernameAndEmail(String username, String email);

    // 아이디, 이메일 중복 확인
    boolean existsByUsername(String username);
    boolean existsByUsernameOrEmail(String username, String email);
}