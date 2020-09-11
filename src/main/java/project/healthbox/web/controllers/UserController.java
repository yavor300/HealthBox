package project.healthbox.web.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import project.healthbox.domain.models.binding.UserLoginBindingModel;
import project.healthbox.domain.models.binding.UserRegisterBindingModel;
import project.healthbox.domain.models.service.UserLoginServiceModel;
import project.healthbox.domain.models.service.UserServiceModel;
import project.healthbox.service.DoctorService;
import project.healthbox.service.UserService;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController extends BaseController {
    private final UserService userService;
    private final ModelMapper modelMapper;
    private final DoctorService doctorServicel;

    @Autowired
    public UserController(UserService userService, ModelMapper modelMapper, DoctorService doctorServicel) {
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.doctorServicel = doctorServicel;
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
    public ModelAndView loginUser(@ModelAttribute UserRegisterBindingModel user, HttpSession httpSession) {
        UserLoginServiceModel loggedUser = null;
        try {
            loggedUser = this.userService.login(this.modelMapper.map(user, UserLoginBindingModel.class));
        } catch (Exception e) {
            return super.redirect("/user" + "/login");
        }
        httpSession.setAttribute("user", loggedUser);
        if (loggedUser.getTitle().equals("Doctor")) {
            if (this.doctorServicel.isAccountCompleted(loggedUser)) {
                return super.redirect("/doctor" + "/home");
            } else {
                return super.redirect("/doctor" + "/complete/" + loggedUser.getId());
            }
        } else {
            return super.redirect("/home");
        }
    }
}
