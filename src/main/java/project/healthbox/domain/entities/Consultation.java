package project.healthbox.domain.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.healthbox.domain.entities.enums.GenderEnum;

import javax.persistence.*;

@Entity
@Table(name = "consultations")
@Getter
@Setter
@NoArgsConstructor
public class Consultation extends BaseEntity {
    @Column(nullable = false)
    private Integer age;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private GenderEnum gender;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String diagnoses;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String medicaments;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String allergy;

    @Column(name = "problem_title", nullable = false)
    private String problemTitle;

    @Column(name = "problem_description", nullable = false, columnDefinition = "TEXT")
    private String problemDescription;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "doctor_id", referencedColumnName = "id")
    private Doctor doctor;

    @OneToOne
    @JoinColumn(name = "answer_id", referencedColumnName = "id")
    private Answer answer;
}
