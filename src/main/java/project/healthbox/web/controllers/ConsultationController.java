package project.healthbox.web.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import project.healthbox.domain.models.binding.ConsultationBindingModel;
import project.healthbox.domain.models.binding.SendAnswerBindingModel;
import project.healthbox.domain.models.service.*;
import project.healthbox.service.AnswerService;
import project.healthbox.service.ConsultationService;
import project.healthbox.service.DoctorService;
import project.healthbox.service.UserService;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/consultation")
public class ConsultationController extends BaseController {
    private final DoctorService doctorService;
    private final ModelMapper modelMapper;
    private final ConsultationService consultationService;
    private final UserService userService;
    private final AnswerService answerService;

    @Autowired
    public ConsultationController(DoctorService doctorService, ModelMapper modelMapper, ConsultationService consultationService, UserService userService, AnswerService answerService) {
        this.doctorService = doctorService;
        this.modelMapper = modelMapper;
        this.consultationService = consultationService;
        this.userService = userService;
        this.answerService = answerService;
    }

    @GetMapping("/send/{id}")
    public ModelAndView getSendConsultationView(@PathVariable String id, ModelAndView modelAndView) {
        modelAndView.addObject("doctorId", id);
        return super.view("consultation/sendConsultation", modelAndView);
    }

    @PostMapping("/send/{id}")
    public ModelAndView sendConsultationForm(@PathVariable String id, @ModelAttribute ConsultationBindingModel consultationBindingModel, HttpSession httpSession) {
        ConsultationServiceModel consultationServiceModel = this.consultationService.save(this.modelMapper.map(consultationBindingModel, ConsultationServiceModel.class));
        UserLoginServiceModel userInSession = (UserLoginServiceModel) httpSession.getAttribute("user");
        UserServiceModel userServiceModel = this.userService.getById(userInSession.getId());
        DoctorServiceModel doctorServiceModel = this.doctorService.getById(id);
        this.consultationService.setDoctorAndUser(consultationServiceModel, doctorServiceModel, userServiceModel);
        return super.redirect("/user" + "/dashboard/" + userServiceModel.getId());
    }

    @GetMapping("/details/{id}")
    public ModelAndView getConsultationDetailsView(@PathVariable String id, ModelAndView modelAndView) {
        modelAndView.addObject("consultation", this.consultationService.getById(id));
        return super.view("consultation/details", modelAndView);
    }

    @GetMapping("/answer/{id}")
    public ModelAndView getConsultationAnswerView(@PathVariable String id, ModelAndView modelAndView) {
        ConsultationServiceModel consultation = this.consultationService.getById(id);
        modelAndView.addObject("consultation", consultation);
        return super.view("consultation/answer", modelAndView);
    }

    @PostMapping("/answer/{id}")
    public ModelAndView answerConsultation(@PathVariable String id, @ModelAttribute SendAnswerBindingModel sendAnswerBindingModel, HttpSession httpSession) {
        AnswerServiceModel answerServiceModel = this.answerService.save(this.modelMapper.map(sendAnswerBindingModel, AnswerServiceModel.class));
        ConsultationServiceModel consultationServiceModel = this.consultationService.getById(id);
        this.consultationService.setAnswer(consultationServiceModel, answerServiceModel);
        UserLoginServiceModel userInSession = (UserLoginServiceModel) httpSession.getAttribute("user");
        DoctorServiceModel doctorServiceModel = this.doctorService.getById(userInSession.getId());
        return super.redirect("/doctor" + "/dashboard/" + doctorServiceModel.getId());
    }
}
