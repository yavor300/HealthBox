package project.healthbox.service.impl;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import project.healthbox.domain.entities.Answer;
import project.healthbox.domain.models.service.AnswerServiceModel;
import project.healthbox.repostory.AnswerRepository;
import project.healthbox.service.AnswerService;

@Service
@AllArgsConstructor
public class AnswerServiceImpl implements AnswerService {
    private final AnswerRepository answerRepository;
    private final ModelMapper modelMapper;

    @Override
    public AnswerServiceModel save(AnswerServiceModel answerServiceModel) {
        return this.modelMapper.map(this.answerRepository.saveAndFlush(
                this.modelMapper.map(answerServiceModel, Answer.class)
        ), AnswerServiceModel.class);
    }
}
