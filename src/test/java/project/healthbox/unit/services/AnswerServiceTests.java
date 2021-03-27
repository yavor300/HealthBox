package project.healthbox.unit.services;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import project.healthbox.domain.entities.Answer;
import project.healthbox.domain.models.service.AnswerServiceModel;
import project.healthbox.repostory.AnswerRepository;
import project.healthbox.service.AnswerService;
import project.healthbox.service.impl.AnswerServiceImpl;

import static org.mockito.ArgumentMatchers.any;

public class AnswerServiceTests {
    private final static String ANSWER_PROBLEM_ANSWER = "ANSWER";
    private final static String ANSWER_ID = "UUID";

    private AnswerService answerService;
    private AnswerRepository mockAnswerRepository;

    @Before
    public void init() {
        mockAnswerRepository = Mockito.mock(AnswerRepository.class);
        answerService = new AnswerServiceImpl(mockAnswerRepository, new ModelMapper());
    }

    @Test
    public void save_Should_Save_And_Return_CityServiceModel() {
        Answer answer = new Answer();
        answer.setId(ANSWER_ID);
        answer.setProblemAnswer(ANSWER_PROBLEM_ANSWER);

        AnswerServiceModel answerServiceModel = new AnswerServiceModel();
        answerServiceModel.setId(ANSWER_ID);
        answerServiceModel.setProblemAnswer(ANSWER_PROBLEM_ANSWER);

        Mockito.when(mockAnswerRepository.saveAndFlush(any(Answer.class)))
                .thenReturn(answer);

        AnswerServiceModel result = answerService.save(answerServiceModel);

        Assert.assertEquals(answerServiceModel.getId(), result.getId());
        Assert.assertEquals(answerServiceModel.getProblemAnswer(), result.getProblemAnswer());
    }
}