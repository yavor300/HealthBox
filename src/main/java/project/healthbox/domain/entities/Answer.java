package project.healthbox.domain.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "answers")
@Getter
@Setter
@NoArgsConstructor
public class Answer extends BaseEntity {
    @Column(name = "problem_answer", nullable = false, columnDefinition = "TEXT")
    private String problemAnswer;
    @OneToOne(mappedBy = "answer")
    private Consultation consultation;
}
