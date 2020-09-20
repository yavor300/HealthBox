package project.healthbox.repostory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.healthbox.domain.entities.City;

@Repository
public interface CityRepository extends JpaRepository<City, String> {
    City getByName(String name);
}
