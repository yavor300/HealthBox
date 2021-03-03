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
import project.healthbox.domain.models.view.DoctorsAllViewModel;
import project.healthbox.domain.models.view.DoctorDashboardViewModel;
import project.healthbox.domain.models.view.DoctorDeleteViewModel;
import project.healthbox.domain.models.view.DoctorProfileViewModel;
import project.healthbox.error.DoctorsNotFoundException;
import project.healthbox.service.CityService;
import project.healthbox.service.DoctorService;
import project.healthbox.service.SpecialtyService;
import project.healthbox.validation.doctor.DoctorUpdateMultipartFileValidator;
import project.healthbox.web.annotations.PageTitle;

import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.util.List;
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

        //TODO ADD VIEW MODELS FOR THAT PAGE
        modelAndView.addObject("doctorId", doctorService.getByEmail(principal.getName()).getId());

        modelAndView.addObject("specialties", specialtyService.getAll());

        modelAndView.addObject("cities", cityService.getAll());

        modelAndView.setViewName("user/doctorUpdate");
        return modelAndView;
    }

    @PostMapping("/complete")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView updateProfile(@Valid @ModelAttribute DoctorUpdateBindingModel doctorUpdateBindingModel, BindingResult bindingResult,
                                      Principal principal, ModelAndView modelAndView,
                                      RedirectAttributes redirectAttributes) throws IOException {

        this.doctorUpdateMultipartFileValidator.validate(doctorUpdateBindingModel, bindingResult);

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("doctorUpdateBindingModel", doctorUpdateBindingModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.doctorUpdateBindingModel", bindingResult);
            modelAndView.setViewName("redirect:complete");
            return modelAndView;
//            modelAndView.addObject("doctorId", this.doctorService.getByEmail(principal.getName()).getId());
//            modelAndView.addObject("specialties", this.specialtyService.getAll());
//            modelAndView.addObject("cities", this.cityService.getAll());
//            modelAndView.addObject("model", model);
//            return super.view("user/doctorUpdate", modelAndView);
        }

        DoctorServiceModel doctorServiceModel = modelMapper.map(doctorUpdateBindingModel, DoctorServiceModel.class);
        doctorServiceModel.setId(this.doctorService.getByEmail(principal.getName()).getId());

        this.doctorService.update(doctorServiceModel);
        modelAndView.setViewName("redirect:/doctor/dashboard");
        return modelAndView;
        //return super.redirect("/doctor" + "/dashboard");
    }

    @GetMapping("/profile/{id}")
    @PreAuthorize("isAuthenticated()")
    @PageTitle("Doctor Profile")
    public ModelAndView getProfileView(@PathVariable String id, ModelAndView modelAndView) {
        DoctorServiceModel doctorServiceModel = this.doctorService.getById(id);
        DoctorProfileViewModel doctor = this.modelMapper.map(doctorServiceModel, DoctorProfileViewModel.class);
        modelAndView.addObject("doctor", doctor);
        modelAndView.setViewName("doctor/profile");
        return modelAndView;
        //return super.view("doctor/profile", modelAndView);
    }

    @GetMapping("/dashboard")
    @PreAuthorize("isAuthenticated()")
    @PageTitle("Dashboard")
    public ModelAndView getDashboardDoctorView(Principal principal, ModelAndView modelAndView) {
        DoctorServiceModel doctor = this.doctorService.getById(this.doctorService.getByEmail(principal.getName()).getId());
        List<DoctorDashboardViewModel> consultations = doctor.getConsultations()
                .stream()
                .map(d -> this.modelMapper.map(d, DoctorDashboardViewModel.class))
                .collect(Collectors.toList());
        modelAndView.addObject("consultations", consultations);

        modelAndView.setViewName("doctor/dashboard");
        return modelAndView;
        //return super.view("doctor/dashboard", modelAndView);
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PageTitle("All Doctors")
    public ModelAndView getAllView(ModelAndView modelAndView) {
        List<DoctorsAllViewModel> doctors = this.doctorService.getAll()
                .stream()
                .map(d -> this.modelMapper.map(d, DoctorsAllViewModel.class))
                .collect(Collectors.toList());
        modelAndView.addObject("doctors", doctors);

        modelAndView.setViewName("doctor/all-doctors");
        return modelAndView;
        //return super.view("doctor/all-doctors", modelAndView);
    }

    @GetMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PageTitle("Delete Doctor")
    public ModelAndView deleteDoctor(@PathVariable String id, ModelAndView modelAndView) {
        DoctorServiceModel doctorServiceModel = this.doctorService.getById(id);
        DoctorDeleteViewModel doctor = this.modelMapper.map(doctorServiceModel, DoctorDeleteViewModel.class);
        modelAndView.addObject("doctor", doctor);

        modelAndView.setViewName("doctor/delete-doctor");
        return modelAndView;
        //return super.view("doctor/delete-doctor", modelAndView);
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView deleteDoctorConfirm(@PathVariable String id, ModelAndView modelAndView) {
        this.doctorService.deleteDoctor(id);
        modelAndView.setViewName("redirect:/doctor/all");
        return modelAndView;
        //return super.redirect("/doctor" + "/all");
    }

    @ExceptionHandler({DoctorsNotFoundException.class})
    public ModelAndView handleDoctorsNotFoundException(DoctorsNotFoundException e) {
        ModelAndView modelAndView = new ModelAndView("error/noDoctorsFound");
        modelAndView.addObject("message", e.getMessage());
        modelAndView.addObject("statusCode", e.getStatusCode());
        return modelAndView;
    }
}
