package project.healthbox.domain.models.service;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AnswerServiceModel extends BaseService {
    private String problemAnswer;
    private ConsultationServiceModel consultation;
}
