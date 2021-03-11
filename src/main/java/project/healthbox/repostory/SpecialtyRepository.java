package project.healthbox.repostory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.healthbox.domain.entities.Specialty;

import java.util.Optional;

@Repository
public interface SpecialtyRepository extends JpaRepository<Specialty, String> {
    Optional<Specialty> findByName(String name);

    Specialty getById(String id);
}
