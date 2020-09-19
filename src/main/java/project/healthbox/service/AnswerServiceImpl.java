package project.healthbox.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.healthbox.domain.entities.Answer;
import project.healthbox.domain.models.service.AnswerServiceModel;
import project.healthbox.repostory.AnswerRepository;

@Service
public class AnswerServiceImpl implements AnswerService {
    private final AnswerRepository answerRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public AnswerServiceImpl(AnswerRepository answerRepository, ModelMapper modelMapper) {
        this.answerRepository = answerRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public AnswerServiceModel save(AnswerServiceModel answerServiceModel) {
        return this.modelMapper.map(this.answerRepository.saveAndFlush(
                this.modelMapper.map(answerServiceModel, Answer.class)
        ), AnswerServiceModel.class);
    }
}
