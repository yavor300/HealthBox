package project.healthbox.web.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import project.healthbox.domain.models.binding.UserLoginBindingModel;
import project.healthbox.domain.models.binding.UserRegisterBindingModel;
import project.healthbox.domain.models.service.ConsultationServiceModel;
import project.healthbox.domain.models.service.UserLoginServiceModel;
import project.healthbox.domain.models.service.UserServiceModel;
import project.healthbox.service.DoctorService;
import project.healthbox.service.UserService;
import project.healthbox.validation.user.UserRegisterValidator;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController extends BaseController {
    private final UserService userService;
    private final ModelMapper modelMapper;
    private final DoctorService doctorService;
    private final UserRegisterValidator userRegisterValidator;

    @Autowired
    public UserController(UserService userService, ModelMapper modelMapper, DoctorService doctorService, UserRegisterValidator userRegisterValidator) {
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.doctorService = doctorService;
        this.userRegisterValidator = userRegisterValidator;
    }

    @GetMapping("/register")
    public ModelAndView getRegisterView(ModelAndView modelAndView, @ModelAttribute(name = "model") UserRegisterBindingModel model) {
        modelAndView.addObject("model", model);

        return super.view("user/register", modelAndView);
    }

    @PostMapping("/register")
    public ModelAndView register(ModelAndView modelAndView, @ModelAttribute(name = "model") UserRegisterBindingModel model, BindingResult bindingResult) {
        this.userRegisterValidator.validate(model, bindingResult);

        if (bindingResult.hasErrors()) {
            model.setPassword(null);
            model.setConfirmPassword(null);
            modelAndView.addObject("model", model);

            return super.view("user/register", modelAndView);
        }


//        if (!user.getPassword().equals(user.getConfirmPassword())) {
//            return super.view("user/register");
//        }
//        try {
//            this.userService.register(this.modelMapper.map(user, UserServiceModel.class));
//        } catch (Exception e) {
//            return super.redirect("/user" + "/register");
//        }
        this.userService.register(this.modelMapper.map(model, UserServiceModel.class));
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
