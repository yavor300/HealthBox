package project.healthbox.web.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import project.healthbox.domain.models.binding.DoctorUpdateBindingModel;
import project.healthbox.domain.models.service.DoctorServiceModel;
import project.healthbox.domain.models.view.AllDoctorsViewModel;
import project.healthbox.domain.models.view.DoctorDashboardViewModel;
import project.healthbox.domain.models.view.DoctorDeleteViewModel;
import project.healthbox.domain.models.view.DoctorProfileViewModel;
import project.healthbox.error.DoctorsNotFoundException;
import project.healthbox.service.CityService;
import project.healthbox.service.DoctorService;
import project.healthbox.service.SpecialtyService;
import project.healthbox.validation.doctor.DoctorUpdateValidator;
import project.healthbox.web.annotations.PageTitle;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/doctor")
public class DoctorController extends BaseController {
    private final DoctorService doctorService;
    private final SpecialtyService specialtyService;
    private final CityService cityService;
    private final DoctorUpdateValidator doctorUpdateValidator;
    private final ModelMapper modelMapper;

    @Autowired
    public DoctorController(DoctorService doctorService, SpecialtyService specialtyService, CityService cityService, DoctorUpdateValidator doctorUpdateValidator, ModelMapper modelMapper) {
        this.doctorService = doctorService;
        this.specialtyService = specialtyService;
        this.cityService = cityService;
        this.doctorUpdateValidator = doctorUpdateValidator;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/complete")
    @PreAuthorize("isAuthenticated()")
    @PageTitle("Complete Doctor Account")
    public ModelAndView getRegisterView(Principal principal, ModelAndView modelAndView, @ModelAttribute(name = "model") DoctorUpdateBindingModel model) {
        modelAndView.addObject("doctorId", this.doctorService.getByEmail(principal.getName()).getId());
        modelAndView.addObject("specialties", this.specialtyService.getAll());
        modelAndView.addObject("cities", this.cityService.getAll());
        modelAndView.addObject("model", model);
        return super.view("user/doctorUpdate", modelAndView);
    }

    @PostMapping("/complete")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView updateProfile(Principal principal, ModelAndView modelAndView, @ModelAttribute(name = "model") DoctorUpdateBindingModel model, BindingResult bindingResult) throws IOException {
        this.doctorUpdateValidator.validate(model, bindingResult);

        if (bindingResult.hasErrors()) {
            modelAndView.addObject("doctorId", this.doctorService.getByEmail(principal.getName()).getId());
            modelAndView.addObject("specialties", this.specialtyService.getAll());
            modelAndView.addObject("cities", this.cityService.getAll());
            modelAndView.addObject("model", model);
            return super.view("user/doctorUpdate", modelAndView);
        }

        model.setId(this.doctorService.getByEmail(principal.getName()).getId());
        this.doctorService.update(model);
        return super.redirect("/doctor" + "/dashboard");
    }

    @GetMapping("/specialty_id={specialtyId}&city_id={cityId}&name={doctorName}")
    @PreAuthorize("isAuthenticated()")
    @PageTitle("Found Doctors")
    public ModelAndView getDoctorsView(@PathVariable String specialtyId, @PathVariable String cityId, @PathVariable String doctorName, HttpSession session) {
        List<DoctorServiceModel> allByGivenCriteria = this.doctorService.findAllByGivenCriteria(specialtyId, cityId, doctorName);

        session.setAttribute("foundDoctors", allByGivenCriteria);

        return super.view("user/doctors");
    }

    @GetMapping("/profile/{id}")
    @PreAuthorize("isAuthenticated()")
    @PageTitle("Doctor Profile")
    public ModelAndView getProfileView(@PathVariable String id, ModelAndView modelAndView) {
        DoctorServiceModel doctorServiceModel = this.doctorService.getById(id);
        DoctorProfileViewModel doctor = this.modelMapper.map(doctorServiceModel, DoctorProfileViewModel.class);
        modelAndView.addObject("doctor", doctor);
        return super.view("doctor/profile", modelAndView);
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
        return super.view("doctor/dashboard", modelAndView);
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PageTitle("All Doctors")
    public ModelAndView getAllView(ModelAndView modelAndView) {
        List<AllDoctorsViewModel> doctors = this.doctorService.getAll()
                .stream()
                .map(d -> this.modelMapper.map(d, AllDoctorsViewModel.class))
                .collect(Collectors.toList());
        modelAndView.addObject("doctors", doctors);
        return super.view("doctor/all-doctors", modelAndView);
    }

    @GetMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PageTitle("Delete Doctor")
    public ModelAndView deleteDoctor(@PathVariable String id, ModelAndView modelAndView) {
        DoctorServiceModel doctorServiceModel = this.doctorService.getById(id);
        DoctorDeleteViewModel doctor = this.modelMapper.map(doctorServiceModel, DoctorDeleteViewModel.class);
        modelAndView.addObject("doctor", doctor);
        return super.view("doctor/delete-doctor", modelAndView);
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView deleteDoctorConfirm(@PathVariable String id) {
        this.doctorService.deleteDoctor(id);
        return super.redirect("/doctor" + "/all");
    }

    @ExceptionHandler({DoctorsNotFoundException.class})
    public ModelAndView handleDoctorsNotFoundException(DoctorsNotFoundException e) {
        ModelAndView modelAndView = new ModelAndView("error/noDoctorsFound");
        modelAndView.addObject("message", e.getMessage());
        modelAndView.addObject("statusCode", e.getStatusCode());
        return modelAndView;
    }
}
