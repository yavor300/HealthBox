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
import project.healthbox.domain.models.view.ConsultationDashboardViewModel;
import project.healthbox.domain.models.view.UserDeleteViewModel;
import project.healthbox.domain.models.view.UsersAllViewModel;
import project.healthbox.events.register.UserRegisterEventPublisher;
import project.healthbox.service.UserService;
import project.healthbox.web.annotations.PageTitle;

import javax.validation.Valid;
import java.security.Principal;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {
    private final UserService userService;
    private final ModelMapper modelMapper;
    private final UserRegisterEventPublisher userRegisterEventPublisher;

    @ModelAttribute("userRegisterBindingModel")
    public UserRegisterBindingModel userRegisterBindingModel() {
        return new UserRegisterBindingModel();
    }

    @ModelAttribute("userAlreadyExists")
    public boolean userAlreadyExists() {
        return false;
    }

    @GetMapping("/register")
    @PreAuthorize("isAnonymous()")
    @PageTitle("Register")
    public ModelAndView getRegisterView(ModelAndView modelAndView) {
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

        UserServiceModel registeredUser = userService.register(modelMapper.map(userRegisterBindingModel, UserServiceModel.class), userRegisterBindingModel.getTitle());

        if (registeredUser == null) {
            redirectAttributes.addFlashAttribute("userRegisterBindingModel", userRegisterBindingModel);
            redirectAttributes.addFlashAttribute("userAlreadyExists", true);
            modelAndView.setViewName("redirect:register");
            return modelAndView;
        }

        userRegisterEventPublisher.publishEvent(registeredUser);
        modelAndView.setViewName("redirect:/user/login");
        return modelAndView;
    }

    @ModelAttribute("bad_credentials")
    public boolean badCredentials() {
        return false;
    }

    @ModelAttribute("email")
    private String email() {
        return null;
    }

    @GetMapping("/login")
    @PreAuthorize("isAnonymous()")
    @PageTitle("Login")
    public ModelAndView getLoginView(ModelAndView modelAndView) {
        modelAndView.setViewName("user/login");
        return modelAndView;
    }

    @PostMapping("/login-error")
    public ModelAndView failedLogin(@ModelAttribute(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY)
                                            String email, ModelAndView modelAndView, RedirectAttributes redirectAttributes) {

        redirectAttributes.addFlashAttribute("bad_credentials", true);
        redirectAttributes.addFlashAttribute("email", email);

        modelAndView.setViewName("redirect:/user/login");
        return modelAndView;
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PageTitle("All Users")
    public ModelAndView getAllView(ModelAndView modelAndView) {
        modelAndView.addObject("users", userService.getAll()
                .stream()
                .map(userServiceModel -> modelMapper.map(userServiceModel, UsersAllViewModel.class))
                .collect(Collectors.toList()));

        modelAndView.setViewName("user/all-users");
        return modelAndView;
    }

    @GetMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PageTitle("Delete User")
    public ModelAndView deleteUser(@PathVariable String id, ModelAndView modelAndView) {
        modelAndView.addObject("user", modelMapper.map(userService.getById(id), UserDeleteViewModel.class));
        modelAndView.setViewName("user/delete-user");
        return modelAndView;
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView deleteUserConfirm(@PathVariable String id, ModelAndView modelAndView) {
        userService.deleteUser(id);
        modelAndView.setViewName("redirect:/user/all");
        return modelAndView;
    }

    @PostMapping("/set-admin/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView setAdminRole(@PathVariable String id, ModelAndView modelAndView) {
        userService.makeAdmin(id);
        modelAndView.setViewName("redirect:/user/all");
        return modelAndView;
    }

    @PostMapping("/set-user/{id}")
    @PreAuthorize("hasRole('ROLE_ROOT')")
    public ModelAndView setUserRole(@PathVariable String id, ModelAndView modelAndView) {
        userService.makeUser(id);
        modelAndView.setViewName("redirect:/user/all");
        return modelAndView;
    }

    @GetMapping("/dashboard")
    @PreAuthorize("isAuthenticated()")
    @PageTitle("Dashboard")
    public ModelAndView getProfileView(Principal principal, ModelAndView modelAndView) {
        modelAndView.addObject("consultations", userService.getByEmail(principal.getName())
                .getConsultations()
                .stream()
                .map(consultationServiceModel -> modelMapper.map(consultationServiceModel, ConsultationDashboardViewModel.class))
                .collect(Collectors.toList()));

        modelAndView.setViewName("user/dashboard");
        return modelAndView;
    }
}