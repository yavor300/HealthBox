package project.healthbox.web.controllers;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import project.healthbox.domain.models.binding.ConsultationSendBindingModel;
import project.healthbox.domain.models.service.ConsultationServiceModel;
import project.healthbox.domain.models.service.DoctorServiceModel;
import project.healthbox.domain.models.service.UserServiceModel;
import project.healthbox.domain.models.view.ConsultationDetailsViewModel;
import project.healthbox.service.ConsultationService;
import project.healthbox.service.DoctorService;
import project.healthbox.service.UserService;
import project.healthbox.web.annotations.PageTitle;

import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequestMapping("/consultation")
@AllArgsConstructor
public class ConsultationController {
    private final DoctorService doctorService;
    private final ModelMapper modelMapper;
    private final ConsultationService consultationService;
    private final UserService userService;

    //private final ConsultationFormValidator consultationFormValidator;

    @ModelAttribute("consultationSendBindingModel")
    public ConsultationSendBindingModel consultationSendBindingModel() {
        return new ConsultationSendBindingModel();
    }

    @GetMapping("/send/{id}")
    @PreAuthorize("isAuthenticated()")
    @PageTitle("Send Consultation")
    public ModelAndView getSendConsultationView(@PathVariable String id, ModelAndView modelAndView) {
        modelAndView.addObject("doctorId", id);
        //modelAndView.addObject("model", model);
        modelAndView.setViewName("consultation/sendConsultation");
        //return super.view("consultation/sendConsultation", modelAndView);
        return modelAndView;
    }

    @PostMapping("/send/{id}")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView sendConsultationForm(@Valid @ModelAttribute ConsultationSendBindingModel consultationSendBindingModel,
                                             BindingResult bindingResult,
                                             RedirectAttributes redirectAttributes,
                                             @PathVariable String id,
                                             ModelAndView modelAndView,
                                             Principal principal) {

        //this.consultationFormValidator.validate(model, bindingResult);

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("consultationSendBindingModel", consultationSendBindingModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.consultationSendBindingModel", bindingResult);
            modelAndView.setViewName("redirect:/consultation/send/" + id);
            return modelAndView;
//            modelAndView.addObject("doctorId", id);
//            modelAndView.addObject("model", model);
//            return super.view("consultation/sendConsultation", modelAndView);
        }

        ConsultationServiceModel consultationServiceModel = this.consultationService.save(this.modelMapper.map(consultationSendBindingModel, ConsultationServiceModel.class));
        UserServiceModel userServiceModel = this.userService.getByEmail(principal.getName());
        DoctorServiceModel doctorServiceModel = this.doctorService.getById(id);
        this.consultationService.setDoctorAndUser(consultationServiceModel, doctorServiceModel, userServiceModel);
        modelAndView.setViewName("redirect:/user/dashboard");
        return modelAndView;
        //return super.redirect("/user" + "/dashboard");
    }

    @GetMapping("/details/{id}")
    @PreAuthorize("isAuthenticated()")
    @PageTitle("Consultation Details")
    public ModelAndView getConsultationDetailsView(@PathVariable String id, ModelAndView modelAndView) {
        ConsultationServiceModel consultationServiceModel = this.consultationService.getById(id);
        ConsultationDetailsViewModel consultation = this.modelMapper.map(consultationServiceModel, ConsultationDetailsViewModel.class);
        modelAndView.addObject("consultation", consultation);
        modelAndView.setViewName("consultation/details");
        return modelAndView;
        //return super.view("consultation/details", modelAndView);
    }
}
