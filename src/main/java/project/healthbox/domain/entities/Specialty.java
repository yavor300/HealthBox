package project.healthbox.domain.entities;

import com.google.gson.annotations.Expose;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "specialties")
@Getter
@Setter
@NoArgsConstructor
public class Specialty extends BaseEntity {
    @Expose
    @Column(nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "specialty")
    private List<Doctor> doctors;

    public Specialty(String name) {
        this.name = name;
    }
}
