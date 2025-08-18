package project.deepdot.routine.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.deepdot.user.domain.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface GoalRepository extends JpaRepository<Goal, Long> {
    List<Goal> findByUser(User user);
    long countByUser(User user);
    Optional<Goal> findByUserAndName(User user, String name);
}
