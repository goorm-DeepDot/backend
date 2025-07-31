package project.deepdot.user.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.deepdot.user.domain.Certification;

@Repository
public interface CertificationRepository extends JpaRepository<Certification, String> {

    Certification findByUserId(String userId);

}
