package project.healthbox.web.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import project.healthbox.domain.models.binding.SendAnswerBindingModel;
import project.healthbox.domain.models.service.AnswerServiceModel;
import project.healthbox.domain.models.service.ConsultationServiceModel;
import project.healthbox.domain.models.view.ConsultationDetailsViewModel;
import project.healthbox.service.AnswerService;
import project.healthbox.service.ConsultationService;
import project.healthbox.validation.answer.AnswerValidator;
import project.healthbox.web.annotations.PageTitle;

@Controller
@RequestMapping("/consultation")
public class AnswerController extends BaseController {
    private final AnswerService answerService;
    private final AnswerValidator answerValidator;
    private final ConsultationService consultationService;
    private final ModelMapper modelMapper;

    public AnswerController(AnswerService answerService, AnswerValidator answerValidator, ConsultationService consultationService, ModelMapper modelMapper) {
        this.answerService = answerService;
        this.answerValidator = answerValidator;
        this.consultationService = consultationService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/answer/{id}")
    @PreAuthorize("isAuthenticated()")
    @PageTitle("Answer Consultation")
    public ModelAndView getConsultationAnswerView(@PathVariable String id, ModelAndView modelAndView, @ModelAttribute(name = "model") SendAnswerBindingModel model) {
        ConsultationServiceModel consultationServiceModel = this.consultationService.getById(id);
        ConsultationDetailsViewModel consultation = this.modelMapper.map(consultationServiceModel, ConsultationDetailsViewModel.class);
        modelAndView.addObject("consultation", consultation);
        modelAndView.addObject("model", model);
        return super.view("consultation/answer", modelAndView);
    }

    @PostMapping("/answer/{id}")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView answerConsultation(@PathVariable String id, ModelAndView modelAndView, @ModelAttribute(name = "model") SendAnswerBindingModel model, BindingResult bindingResult) {

        this.answerValidator.validate(model, bindingResult);

        if (bindingResult.hasErrors()) {
            ConsultationServiceModel consultationServiceModel = this.consultationService.getById(id);
            ConsultationDetailsViewModel consultation = this.modelMapper.map(consultationServiceModel, ConsultationDetailsViewModel.class);
            modelAndView.addObject("consultation", consultation);
            modelAndView.addObject("model", model);
            return super.view("consultation/answer", modelAndView);
        }

        AnswerServiceModel answerServiceModel = this.answerService.save(this.modelMapper.map(model, AnswerServiceModel.class));
        ConsultationServiceModel consultationServiceModel = this.consultationService.getById(id);

        this.consultationService.setAnswer(consultationServiceModel, answerServiceModel);

        return super.redirect("/doctor" + "/dashboard");
    }
}
