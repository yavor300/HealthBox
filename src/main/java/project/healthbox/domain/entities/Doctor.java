package project.healthbox.domain.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "doctors")
@Getter
@Setter
@NoArgsConstructor
public class Doctor extends BaseEntity {
    @Column(name = "first_name", nullable = false)
    private String firstName;
    @Column(name = "last_name", nullable = false)
    private String lastName;
    @Column(name = "title", nullable = false)
    private String title;
    @Column(name = "email", nullable = false, unique = true)
    private String email;
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "location")
    private String location;
    @Column(name = "education")
    private String education;
    @Column(name = "biography")
    private String biography;
    @Column(name = "workHistory")
    private String workHistory;
    @ManyToOne
    @JoinColumn(name = "specialty_id", referencedColumnName = "id")
    private Specialty specialty;
    @OneToMany(mappedBy = "doctor")
    private List<Consultation> consultations;
    @ManyToMany(mappedBy = "doctors")
    private List<User> users;
}
