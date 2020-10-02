package project.healthbox.web.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import project.healthbox.domain.models.binding.UserRegisterBindingModel;
import project.healthbox.domain.models.service.UserServiceModel;
import project.healthbox.domain.models.view.AllUsersViewModel;
import project.healthbox.domain.models.view.DeleteUserViewModel;
import project.healthbox.domain.models.view.UserDashboardViewModel;
import project.healthbox.service.UserService;
import project.healthbox.validation.user.UserRegisterValidator;
import project.healthbox.web.annotations.PageTitle;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/user")
public class UserController extends BaseController {
    private final UserService userService;
    private final ModelMapper modelMapper;
    private final UserRegisterValidator userRegisterValidator;

    @Autowired
    public UserController(UserService userService, ModelMapper modelMapper, UserRegisterValidator userRegisterValidator) {
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.userRegisterValidator = userRegisterValidator;
    }

    @GetMapping("/register")
    @PreAuthorize("isAnonymous()")
    @PageTitle("Register")
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
    @PageTitle("Login")
    public ModelAndView getLoginView() {
        return super.view("user/login");
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PageTitle("All Users")
    public ModelAndView getAllView(ModelAndView modelAndView) {
        List<AllUsersViewModel> users = this.userService.getAll()
                .stream()
                .map(u -> this.modelMapper.map(u, AllUsersViewModel.class))
                .collect(Collectors.toList());
        modelAndView.addObject("users", users);
        return super.view("user/all-users", modelAndView);
    }

    @GetMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PageTitle("Delete User")
    public ModelAndView deleteUser(@PathVariable String id, ModelAndView modelAndView) {
        UserServiceModel userServiceModel = this.userService.getById(id);
        DeleteUserViewModel user = this.modelMapper.map(userServiceModel, DeleteUserViewModel.class);
        modelAndView.addObject("user", user);
        return super.view("user/delete-user", modelAndView);
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView deleteUserConfirm(@PathVariable String id) {
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

    @GetMapping("/dashboard")
    @PreAuthorize("isAuthenticated()")
    @PageTitle("Dashboard")
    public ModelAndView getProfileView(Principal principal, ModelAndView modelAndView) {
        UserServiceModel user = this.userService.getByEmail(principal.getName());
        List<UserDashboardViewModel> consultations = user.getConsultations()
                .stream()
                .map(u -> this.modelMapper.map(u, UserDashboardViewModel.class))
                .collect(Collectors.toList());
        modelAndView.addObject("consultations", consultations);
        return super.view("user/dashboard", modelAndView);
    }

}
