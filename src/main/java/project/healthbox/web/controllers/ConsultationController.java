package project.healthbox.web.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import project.healthbox.domain.models.binding.ConsultationBindingModel;
import project.healthbox.domain.models.binding.SendAnswerBindingModel;
import project.healthbox.domain.models.service.*;
import project.healthbox.service.AnswerService;
import project.healthbox.service.ConsultationService;
import project.healthbox.service.DoctorService;
import project.healthbox.service.UserService;
import project.healthbox.validation.answer.AnswerValidator;
import project.healthbox.validation.consultation.ConsultationFormValidator;
import project.healthbox.web.annotations.PageTitle;

import javax.servlet.http.HttpSession;
import java.security.Principal;

@Controller
@RequestMapping("/consultation")
public class ConsultationController extends BaseController {
    private final DoctorService doctorService;
    private final ModelMapper modelMapper;
    private final ConsultationService consultationService;
    private final UserService userService;
    private final AnswerService answerService;
    private final ConsultationFormValidator consultationFormValidator;
    private final AnswerValidator answerValidator;

    @Autowired
    public ConsultationController(DoctorService doctorService, ModelMapper modelMapper, ConsultationService consultationService, UserService userService, AnswerService answerService, ConsultationFormValidator consultationFormValidator, AnswerValidator answerValidator) {
        this.doctorService = doctorService;
        this.modelMapper = modelMapper;
        this.consultationService = consultationService;
        this.userService = userService;
        this.answerService = answerService;
        this.consultationFormValidator = consultationFormValidator;
        this.answerValidator = answerValidator;
    }

    @GetMapping("/send/{id}")
    @PreAuthorize("isAuthenticated()")
    @PageTitle("Send Consultation")
    public ModelAndView getSendConsultationView(@PathVariable String id, ModelAndView modelAndView, @ModelAttribute(name = "model") ConsultationBindingModel model) {
        modelAndView.addObject("doctorId", id);
        modelAndView.addObject("model", model);
        return super.view("consultation/sendConsultation", modelAndView);
    }

    @PostMapping("/send/{id}")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView sendConsultationForm(@PathVariable String id, ModelAndView modelAndView, @ModelAttribute(name = "model") ConsultationBindingModel model, BindingResult bindingResult, Principal principal) {

        this.consultationFormValidator.validate(model, bindingResult);

        if (bindingResult.hasErrors()) {
            modelAndView.addObject("doctorId", id);
            modelAndView.addObject("model", model);
            return super.view("consultation/sendConsultation", modelAndView);
        }

        ConsultationServiceModel consultationServiceModel = this.consultationService.save(this.modelMapper.map(model, ConsultationServiceModel.class));
        UserServiceModel userServiceModel = this.userService.getByEmail(principal.getName());
        DoctorServiceModel doctorServiceModel = this.doctorService.getById(id);
        this.consultationService.setDoctorAndUser(consultationServiceModel, doctorServiceModel, userServiceModel);
        return super.redirect("/user" + "/dashboard");
    }

    @GetMapping("/details/{id}")
    @PreAuthorize("isAuthenticated()")
    @PageTitle("Consultation Details")
    public ModelAndView getConsultationDetailsView(@PathVariable String id, ModelAndView modelAndView) {
        modelAndView.addObject("consultation", this.consultationService.getById(id));
        return super.view("consultation/details", modelAndView);
    }

    @GetMapping("/answer/{id}")
    @PreAuthorize("isAuthenticated()")
    @PageTitle("Answer Consultation")
    public ModelAndView getConsultationAnswerView(@PathVariable String id, ModelAndView modelAndView, @ModelAttribute(name = "model") SendAnswerBindingModel model) {
        ConsultationServiceModel consultation = this.consultationService.getById(id);
        modelAndView.addObject("consultation", consultation);
        modelAndView.addObject("model", model);
        return super.view("consultation/answer", modelAndView);
    }

    @PostMapping("/answer/{id}")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView answerConsultation(@PathVariable String id, ModelAndView modelAndView, @ModelAttribute(name = "model") SendAnswerBindingModel model, BindingResult bindingResult) {

        this.answerValidator.validate(model, bindingResult);

        if (bindingResult.hasErrors()) {
            ConsultationServiceModel consultation = this.consultationService.getById(id);
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
