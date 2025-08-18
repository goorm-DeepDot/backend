package project.deepdot.routine.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.deepdot.user.domain.User;

import java.util.List;

@Repository
public interface RoutineRepository extends JpaRepository<Routine, Long> {

    // 목표가 어떤 루틴이라도 참조 중인지 확인
    boolean existsByGoal_GoalId(Long goalId);

    // 필요 시 사용자별 조회
    List<Routine> findByUser(User user);

    // 특정 사용자 + 특정 목표의 루틴 목록
    List<Routine> findByUserAndGoal(User user, Goal goal);
}