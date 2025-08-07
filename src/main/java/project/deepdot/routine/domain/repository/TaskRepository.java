package project.deepdot.routine.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.deepdot.routine.domain.Routine;
import project.deepdot.routine.domain.Task;
import project.deepdot.user.user.domain.User;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    // Routine 엔티티 기준으로 Task 리스트 조회
    List<Task> findByRoutine(Routine routine);

    List<Task> findByRoutine_User(User routineUser);
}
