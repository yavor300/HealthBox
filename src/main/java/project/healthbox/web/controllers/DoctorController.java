package project.healthbox.web.controllers;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import project.healthbox.domain.models.binding.DoctorUpdateBindingModel;
import project.healthbox.domain.models.service.DoctorServiceModel;
import project.healthbox.domain.models.view.*;
import project.healthbox.error.CityNotFoundException;
import project.healthbox.error.DoctorsNotFoundException;
import project.healthbox.service.CityService;
import project.healthbox.service.DoctorService;
import project.healthbox.service.SpecialtyService;
import project.healthbox.validation.doctor.DoctorUpdateMultipartFileValidator;
import project.healthbox.web.annotations.PageTitle;

import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/doctor")
@AllArgsConstructor
public class DoctorController {
    private final DoctorService doctorService;
    private final SpecialtyService specialtyService;
    private final CityService cityService;
    private final DoctorUpdateMultipartFileValidator doctorUpdateMultipartFileValidator;
    private final ModelMapper modelMapper;

    @ModelAttribute("doctorUpdateBindingModel")
    public DoctorUpdateBindingModel doctorUpdateBindingModel() {
        return new DoctorUpdateBindingModel();
    }

    @GetMapping("/complete")
    @PreAuthorize("isAuthenticated()")
    @PageTitle("Complete Doctor Account")
    public ModelAndView getRegisterView(Principal principal, ModelAndView modelAndView) {
        modelAndView.addObject("doctorId", doctorService.getByEmail(principal.getName()).getId());

        modelAndView.addObject("specialties", specialtyService.getAll()
                .stream()
                .map(specialtyServiceModel -> modelMapper.map(specialtyServiceModel, SpecialtyDoctorCompleteViewModel.class))
                .collect(Collectors.toList()));

        modelAndView.addObject("cities", cityService.getAll()
                .stream()
                .map(cityServiceModel -> modelMapper.map(cityServiceModel, CityDoctorCompleteViewModel.class))
                .collect(Collectors.toList()));

        modelAndView.setViewName("user/doctorUpdate");
        return modelAndView;
    }

    @PostMapping("/complete")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView updateProfile(@Valid @ModelAttribute DoctorUpdateBindingModel doctorUpdateBindingModel, BindingResult bindingResult,
                                      Principal principal, ModelAndView modelAndView,
                                      RedirectAttributes redirectAttributes) throws IOException {

        doctorUpdateMultipartFileValidator.validate(doctorUpdateBindingModel, bindingResult);

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("doctorUpdateBindingModel", doctorUpdateBindingModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.doctorUpdateBindingModel", bindingResult);
            modelAndView.setViewName("redirect:complete");
            return modelAndView;
        }

        DoctorServiceModel doctorServiceModel = modelMapper.map(doctorUpdateBindingModel, DoctorServiceModel.class);
        doctorServiceModel.setId(doctorService.getByEmail(principal.getName()).getId());
        doctorService.update(doctorServiceModel);

        modelAndView.setViewName("redirect:/doctor/dashboard");
        return modelAndView;
    }

    @GetMapping("/profile/{id}")
    @PreAuthorize("isAuthenticated()")
    @PageTitle("Doctor Profile")
    public ModelAndView getProfileView(@PathVariable String id, ModelAndView modelAndView) {
        modelAndView.addObject("doctor", modelMapper.map(doctorService.getById(id), DoctorProfileViewModel.class));
        modelAndView.setViewName("doctor/profile");
        return modelAndView;
    }

    @GetMapping("/dashboard")
    @PreAuthorize("isAuthenticated()")
    @PageTitle("Dashboard")
    public ModelAndView getDashboardDoctorView(Principal principal, ModelAndView modelAndView) {
        modelAndView.addObject("consultations", doctorService.getByEmail(principal.getName()).getConsultations()
                .stream()
                .map(consultationServiceModel -> modelMapper.map(consultationServiceModel, DoctorDashboardViewModel.class))
                .collect(Collectors.toList()));

        modelAndView.setViewName("doctor/dashboard");
        return modelAndView;
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PageTitle("All Doctors")
    public ModelAndView getAllView(ModelAndView modelAndView) {
        modelAndView.addObject("doctors", doctorService.getAll()
                .stream()
                .map(doctorServiceModel -> modelMapper.map(doctorServiceModel, DoctorsAllViewModel.class))
                .collect(Collectors.toList()));

        modelAndView.setViewName("doctor/all-doctors");
        return modelAndView;
    }

    @GetMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PageTitle("Delete Doctor")
    public ModelAndView deleteDoctor(@PathVariable String id, ModelAndView modelAndView) {
        modelAndView.addObject("doctor", modelMapper.map(doctorService.getById(id), DoctorDeleteViewModel.class));
        modelAndView.setViewName("doctor/delete-doctor");
        return modelAndView;
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView deleteDoctorConfirm(@PathVariable String id, ModelAndView modelAndView) {
        this.doctorService.deleteDoctor(id);
        modelAndView.setViewName("redirect:/doctor/all");
        return modelAndView;
    }

    @ExceptionHandler({DoctorsNotFoundException.class})
    public ModelAndView handleDoctorsNotFoundException(DoctorsNotFoundException e) {
        ModelAndView modelAndView = new ModelAndView("error/noDoctorsFound");
        modelAndView.addObject("message", e.getMessage());
        modelAndView.addObject("statusCode", e.getStatusCode());
        return modelAndView;
    }

    @ExceptionHandler({CityNotFoundException.class})
    public ModelAndView handleCityNotFoundException(CityNotFoundException e) {
        ModelAndView modelAndView = new ModelAndView("error/city-not-found");
        modelAndView.addObject("message", e.getMessage());
        return modelAndView;
    }
}