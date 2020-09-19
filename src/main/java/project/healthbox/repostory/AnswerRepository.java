package project.healthbox.repostory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.healthbox.domain.entities.Answer;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, String> {
}
