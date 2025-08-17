package project.deepdot.mainpage.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface FocusDailyRepository extends JpaRepository<FocusDaily, Long> {
    Optional<FocusDaily> findByUserIdAndLocalDate(Long userId, LocalDate localDate);
    List<FocusDaily> findByUserIdAndLocalDateBetweenOrderByLocalDateAsc(
            Long userId, LocalDate from, LocalDate to);
}
