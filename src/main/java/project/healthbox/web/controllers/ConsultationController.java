package project.healthbox.web.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import project.healthbox.domain.models.binding.ConsultationBindingModel;
import project.healthbox.domain.models.service.ConsultationServiceModel;
import project.healthbox.domain.models.service.DoctorServiceModel;
import project.healthbox.domain.models.service.UserLoginServiceModel;
import project.healthbox.domain.models.service.UserServiceModel;
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

    public ConsultationController(DoctorService doctorService, ModelMapper modelMapper, ConsultationService consultationService, UserService userService) {
        this.doctorService = doctorService;
        this.modelMapper = modelMapper;
        this.consultationService = consultationService;
        this.userService = userService;
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
        //this.userService.addConsultation(consultationServiceModel, userServiceModel);
        //this.doctorService.addConsultation(consultationServiceModel, doctorServiceModel);
        this.consultationService.setDoctorAndUser(consultationServiceModel, doctorServiceModel, userServiceModel);


        return super.redirect("/user" + "/profile/" + userServiceModel.getId());
    }
}
