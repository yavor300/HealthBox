package project.healthbox.repostory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.healthbox.domain.entities.Doctor;

import javax.print.Doc;
import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, String> {
    Doctor getById(String id);

    Optional<Doctor> findByEmailAndPassword(String email, String password);

    List<Doctor> findBySpecialtyName(String specialty);

    List<Doctor> findAllByFirstNameAndLastName(String firstName, String lastName);

    List<Doctor> findAllByLocationId(String id);

    List<Doctor> findAllBySpecialtyId(String id);

    List<Doctor> findAllByLocationIdAndFirstNameAndLastName(String locationId, String firstName, String lastName);

    List<Doctor> findAllBySpecialtyIdAndFirstNameAndLastName(String id, String firstName, String lastName);

    List<Doctor> findAllBySpecialtyIdAndLocationId(String specialtyId, String locationId);

    List<Doctor> findAllBySpecialtyIdAndLocationIdAndFirstNameAndLastName(String specialtyId, String locationId, String firstName, String lastName);

    boolean existsByEmail(String email);
}
