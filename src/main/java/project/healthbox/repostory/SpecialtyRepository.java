package project.healthbox.repostory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.healthbox.domain.entities.Specialty;

@Repository
public interface SpecialtyRepository extends JpaRepository<Specialty, String> {
    Specialty findByName(String name);
}
