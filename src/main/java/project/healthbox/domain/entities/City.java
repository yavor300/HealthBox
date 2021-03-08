package project.healthbox.domain.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "cities")
@Getter
@Setter
@NoArgsConstructor
public class City extends BaseEntity {
    @Column(nullable = false, unique = true)
    private String name;
    @OneToMany(mappedBy = "location")
    private List<Doctor> doctors;

    public City(String name) {
        this.name = name;
    }
}
