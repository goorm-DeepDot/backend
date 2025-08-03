package project.deepdot.user.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.deepdot.user.domain.Routine;

@Repository
public interface RoutineRepository extends JpaRepository<Routine, Long> {
}
