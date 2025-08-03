package project.deepdot.schedule.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import project.deepdot.user.domain.User;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findAllByUser(User user);
}