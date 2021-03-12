package project.healthbox.repostory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.healthbox.domain.entities.Doctor;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, String> {
    Optional<Doctor> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query("SELECT d FROM Doctor d WHERE (length(:specialty_id) = 0  or d.specialty.id = :specialty_id)" +
            " AND (length(:location_id) = 0 or d.location.id = :location_id) " +
            "AND (length(:firstName) = 0 or d.firstName = :firstName) " +
            "AND (length(:lastName) = 0 or d.lastName = :lastName)")
    List<Doctor> findAllBySpecialtyIdAndLocationIdAndFirstNameAndLastName(@Param("specialty_id") String specialtyId,
                                                                          @Param("location_id") String locationId,
                                                                          @Param("firstName") String firstName,
                                                                          @Param("lastName") String lastName);

}