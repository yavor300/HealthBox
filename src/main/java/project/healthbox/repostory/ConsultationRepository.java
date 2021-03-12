package project.healthbox.repostory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.healthbox.domain.entities.Consultation;

@Repository
public interface ConsultationRepository extends JpaRepository<Consultation, String> {
}
