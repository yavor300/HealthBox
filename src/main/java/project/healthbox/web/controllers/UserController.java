package project.healthbox.web.controllers;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import project.healthbox.domain.models.binding.UserRegisterBindingModel;
import project.healthbox.domain.models.service.UserServiceModel;
import project.healthbox.domain.models.view.AllUsersViewModel;
import project.healthbox.domain.models.view.DeleteUserViewModel;
import project.healthbox.domain.models.view.UserDashboardViewModel;
import project.healthbox.service.UserService;
import project.healthbox.web.annotations.PageTitle;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/user")
@AllArgsConstructor
public class UserController extends BaseController {
    private final UserService userService;
    private final ModelMapper modelMapper;

    @ModelAttribute("userRegisterBindingModel")
    public UserRegisterBindingModel userRegisterBindingModel() {
        return new UserRegisterBindingModel();
    }

    @GetMapping("/register")
    @PreAuthorize("isAnonymous()")
    @PageTitle("Register")
    public ModelAndView getRegisterView(ModelAndView modelAndView) {
        if (modelAndView.getModel().containsKey("passwordsNotEqual")) {
            modelAndView.addObject("passwordsNotEqual", false);
        }

        if (modelAndView.getModel().containsKey("userAlreadyExists")) {
            modelAndView.addObject("userAlreadyExists", false);
        }

        modelAndView.setViewName("user/register");
        return modelAndView;
    }

    @PostMapping("/register")
    @PreAuthorize("isAnonymous()")
    public ModelAndView registerConfirm(@Valid @ModelAttribute UserRegisterBindingModel userRegisterBindingModel, BindingResult bindingResult,
                                        RedirectAttributes redirectAttributes, ModelAndView modelAndView) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("userRegisterBindingModel", userRegisterBindingModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.userRegisterBindingModel", bindingResult);
            modelAndView.setViewName("redirect:register");
            return modelAndView;
        }

        if (!userRegisterBindingModel.getPassword().equals(userRegisterBindingModel.getConfirmPassword())) {
            redirectAttributes.addFlashAttribute("userRegisterBindingModel", userRegisterBindingModel);
            redirectAttributes.addFlashAttribute("passwordsNotEqual", true);
            modelAndView.setViewName("redirect:register");
            return modelAndView;
        }

        UserServiceModel registeredUser = this.userService.register(this.modelMapper.map(userRegisterBindingModel, UserServiceModel.class));

        if (registeredUser == null) {
            redirectAttributes.addFlashAttribute("userRegisterBindingModel", userRegisterBindingModel);
            redirectAttributes.addFlashAttribute("userAlreadyExists", true);
            modelAndView.setViewName("redirect:register");
            return modelAndView;
        }

        modelAndView.setViewName("redirect:/user/login");
        return modelAndView;
    }

    @GetMapping("/login")
    @PreAuthorize("isAnonymous()")
    @PageTitle("Login")
    public ModelAndView getLoginView(@RequestParam(required = false, name = "error") boolean error, ModelAndView modelAndView) {
        modelAndView.addObject("error", error);
        modelAndView.setViewName("user/login");
        return modelAndView;
    }

//    @PostMapping("/login-error")
//    public ModelAndView onLoginError(
//            @ModelAttribute(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY) String user) {
//        ModelAndView modelAndView = new ModelAndView();
//
//        modelAndView.addObject("error", "bad.credentials");
//        modelAndView.addObject("username", user);
//
//        modelAndView.setViewName("user/login");
//
//        return modelAndView;
//    }


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
