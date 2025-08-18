package project.deepdot.setting.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AlarmSettingsRepository extends JpaRepository<AlarmSettings, Long> {
    Optional<AlarmSettings> findByUserId(Long userId); // [NEW]
}