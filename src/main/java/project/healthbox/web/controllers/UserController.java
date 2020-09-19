package project.healthbox.web.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import project.healthbox.domain.models.binding.DoctorUpdateBindingModel;
import project.healthbox.domain.models.binding.UserLoginBindingModel;
import project.healthbox.domain.models.binding.UserRegisterBindingModel;
import project.healthbox.domain.models.service.ConsultationServiceModel;
import project.healthbox.domain.models.service.DoctorServiceModel;
import project.healthbox.domain.models.service.UserLoginServiceModel;
import project.healthbox.domain.models.service.UserServiceModel;
import project.healthbox.domain.models.view.FoundDoctorViewModel;
import project.healthbox.repostory.SpecialtyRepository;
import project.healthbox.service.DoctorService;
import project.healthbox.service.SpecialtyService;
import project.healthbox.service.UserService;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/user")
public class UserController extends BaseController {
    private final UserService userService;
    private final ModelMapper modelMapper;
    private final DoctorService doctorService;
    private final SpecialtyRepository specialtyRepository;
    private final SpecialtyService specialtyService;

    @Autowired
    public UserController(UserService userService, ModelMapper modelMapper, DoctorService doctorService, SpecialtyRepository specialtyRepository, SpecialtyService specialtyService) {
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.doctorService = doctorService;
        this.specialtyRepository = specialtyRepository;
        this.specialtyService = specialtyService;
    }

    @GetMapping("/register")
    public ModelAndView getRegisterView() {
        return super.view("user/register");
    }

    @PostMapping("/register")
    public ModelAndView register(@ModelAttribute UserRegisterBindingModel user) {
        if (!user.getPassword().equals(user.getConfirmPassword())) {
            return super.view("user/register");
        }
        try {
            this.userService.register(this.modelMapper.map(user, UserServiceModel.class));
        } catch (Exception e) {
            return super.redirect("/user" + "/register");
        }
        return super.redirect("/user" + "/login");
    }

    @GetMapping("/login")
    public ModelAndView getLoginView() {
        return super.view("user/login");
    }

    @PostMapping("/login")
    public ModelAndView loginUser(@ModelAttribute UserRegisterBindingModel user, HttpSession httpSession, ModelAndView modelAndView) {
        UserLoginServiceModel loggedUser = null;
        try {
            loggedUser = this.userService.login(this.modelMapper.map(user, UserLoginBindingModel.class));
        } catch (Exception e) {
            return super.redirect("/user" + "/login");
        }
        httpSession.setAttribute("user", loggedUser);
        if (loggedUser.getTitle().equals("Doctor")) {
            if (this.doctorService.isAccountCompleted(loggedUser)) {
                return super.redirect("/doctor" + "/dashboard/" + loggedUser.getId());
            } else {
                return super.redirect("/doctor" + "/complete/" + loggedUser.getId());
            }
        } else {
            return super.redirect("/home");
        }
    }

    @GetMapping("/doctors")
    public ModelAndView getDoctorsView() {
        return super.view("user/doctors");
    }





    @GetMapping("/noDoctorsFound")
    public ModelAndView getNoDoctorsFoundView() {
        return super.view("user/noDoctorsFound");
    }

    @GetMapping("/dashboard/{id}")
    public ModelAndView getProfileView(@PathVariable String id, ModelAndView modelAndView, HttpSession httpSession) {
        UserServiceModel user = this.userService.getById(id);
        List<ConsultationServiceModel> consultations = user.getConsultations();
        modelAndView.addObject("consultations", consultations);
        return super.view("user/dashboard", modelAndView);
    }

}
