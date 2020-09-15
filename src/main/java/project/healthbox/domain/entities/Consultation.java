package project.healthbox.domain.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "consultations")
@Getter
@Setter
@NoArgsConstructor
public class Consultation extends BaseEntity {
    @Column(name = "age", nullable = false)
    private Integer age;
    @Column(name = "gender", nullable = false)
    private String gender;
    @Column(name = "diagnoses", nullable = false)
    private String diagnoses;
    @Column(name = "medicaments", nullable = false)
    private String medicaments;
    @Column(name = "allergy", nullable = false)
    private String allergy;
    @Column(name = "problem_description", nullable = false)
    private String problemDescription;
    @ManyToOne
    @JoinColumn(name = "consultaton_id", referencedColumnName = "id")
    private User user;
}
