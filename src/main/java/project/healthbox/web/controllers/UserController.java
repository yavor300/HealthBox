package project.healthbox.web.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import project.healthbox.domain.models.binding.UserRegisterBindingModel;
import project.healthbox.domain.models.service.ConsultationServiceModel;
import project.healthbox.domain.models.service.UserServiceModel;
import project.healthbox.service.DoctorService;
import project.healthbox.service.UserService;
import project.healthbox.validation.user.UserRegisterValidator;

import java.security.Principal;
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
    @PreAuthorize("isAnonymous()")
    public ModelAndView getRegisterView(ModelAndView modelAndView, @ModelAttribute(name = "model") UserRegisterBindingModel model) {
        modelAndView.addObject("model", model);

        return super.view("user/register", modelAndView);
    }

    @PostMapping("/register")
    @PreAuthorize("isAnonymous()")
    public ModelAndView register(ModelAndView modelAndView, @ModelAttribute(name = "model") UserRegisterBindingModel model, BindingResult bindingResult) {
        this.userRegisterValidator.validate(model, bindingResult);

        if (bindingResult.hasErrors()) {
            model.setPassword(null);
            model.setConfirmPassword(null);
            modelAndView.addObject("model", model);

            return super.view("user/register", modelAndView);
        }

        this.userService.register(this.modelMapper.map(model, UserServiceModel.class));
        return super.redirect("/user" + "/login");
    }

    @GetMapping("/login")
    @PreAuthorize("isAnonymous()")
    public ModelAndView getLoginView() {
        return super.view("user/login");
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView getAllView(ModelAndView modelAndView) {
        modelAndView.addObject("users", this.userService.getAll());
        return super.view("user/all-users", modelAndView);
    }

    @GetMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView deleteQuote(@PathVariable String id, ModelAndView modelAndView) {
        UserServiceModel user = this.userService.getById(id);
        modelAndView.addObject("user", user);
        return super.view("user/delete-user", modelAndView);
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView deleteQuoteConfirm(@PathVariable String id) {
        this.userService.deleteUser(id);
        return super.redirect("/user" + "/all");
    }

    @PostMapping("/set-admin/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView setAdminRole(@PathVariable String id) {
        this.userService.makeAdmin(id);
        return super.redirect("/user" + "/all");
    }

    @PostMapping("/set-user/{id}")
    @PreAuthorize("hasRole('ROLE_ROOT')")
    public ModelAndView setUserRole(@PathVariable String id) {
        this.userService.makeUser(id);

        return super.redirect("/user" + "/all");
    }
/*
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
            if (this.doctorService.isAccountCompleted(loggedUser)) {
                return super.redirect("/doctor" + "/dashboard/" + loggedUser.getId());
            } else {
                return super.redirect("/doctor" + "/complete/" + loggedUser.getId());
            }
        } else {
            return super.redirect("/home");
        }
    }

 */

    @GetMapping("/dashboard")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView getProfileView(Principal principal, ModelAndView modelAndView) {
        UserServiceModel user = this.userService.getByEmail(principal.getName());
        List<ConsultationServiceModel> consultations = user.getConsultations();
        modelAndView.addObject("consultations", consultations);
        return super.view("user/dashboard", modelAndView);
    }

}
