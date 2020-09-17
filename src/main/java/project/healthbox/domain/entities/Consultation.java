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
    @Column(name = "problem_title", nullable = false)
    private String problemTitle;
    @Column(name = "problem_description", nullable = false)
    private String problemDescription;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "doctor_id", referencedColumnName = "id")
    private Doctor doctor;
}