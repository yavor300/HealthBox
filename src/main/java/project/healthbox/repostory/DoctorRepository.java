package project.healthbox.repostory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.healthbox.domain.entities.Doctor;

import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, String> {
    Doctor getById(String id);
    Optional<Doctor> findByEmailAndPassword(String email, String password);
    boolean existsByEmail(String email);
}
