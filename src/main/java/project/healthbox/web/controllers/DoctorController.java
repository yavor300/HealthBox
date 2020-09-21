package project.healthbox.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import project.healthbox.domain.models.binding.DoctorUpdateBindingModel;
import project.healthbox.domain.models.service.ConsultationServiceModel;
import project.healthbox.domain.models.service.DoctorServiceModel;
import project.healthbox.service.CityService;
import project.healthbox.service.DoctorService;
import project.healthbox.service.SpecialtyService;
import project.healthbox.validation.doctor.DoctorUpdateValidator;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/doctor")
public class DoctorController extends BaseController {
    private final DoctorService doctorService;
    private final SpecialtyService specialtyService;
    private final CityService cityService;
    private final DoctorUpdateValidator doctorUpdateValidator;

    @Autowired
    public DoctorController(DoctorService doctorService, SpecialtyService specialtyService, CityService cityService, DoctorUpdateValidator doctorUpdateValidator) {
        this.doctorService = doctorService;
        this.specialtyService = specialtyService;
        this.cityService = cityService;
        this.doctorUpdateValidator = doctorUpdateValidator;
    }

    @GetMapping("/complete/{id}")
    public ModelAndView getRegisterView(@PathVariable String id, ModelAndView modelAndView, @ModelAttribute(name = "model") DoctorUpdateBindingModel model) {
        modelAndView.addObject("doctorId", id);
        modelAndView.addObject("specialties", this.specialtyService.getAll());
        modelAndView.addObject("cities", this.cityService.getAll());
        modelAndView.addObject("model", model);
        return super.view("user/doctorUpdate", modelAndView);
    }

    @PostMapping("/complete/{id}")
    public ModelAndView updateProfile(@PathVariable String id, ModelAndView modelAndView, @ModelAttribute(name = "model") DoctorUpdateBindingModel model, BindingResult bindingResult) {
        this.doctorUpdateValidator.validate(model, bindingResult);

        if (bindingResult.hasErrors()) {
            modelAndView.addObject("doctorId", id);
            modelAndView.addObject("specialties", this.specialtyService.getAll());
            modelAndView.addObject("cities", this.cityService.getAll());
            modelAndView.addObject("model", model);
            return super.view("user/doctorUpdate", modelAndView);
        }

        model.setId(id);
        this.doctorService.update(model);
        return super.redirect("/doctor" + "/dashboard/" + id);
    }

    @GetMapping("/specialty_id={specialtyId}&city_id={cityId}&name={doctorName}")
    public ModelAndView getDoctorsView(@PathVariable String specialtyId, @PathVariable String cityId, @PathVariable String doctorName, HttpSession session) {

        List<DoctorServiceModel> allByGivenCriteria = this.doctorService.findAllByGivenCriteria(specialtyId, cityId, doctorName);

        if (allByGivenCriteria == null) {
            return super.view("user/noDoctorsFound");
        }

        session.setAttribute("foundDoctors", allByGivenCriteria);

        return super.view("user/doctors");
    }

    @GetMapping("/noDoctorsFound")
    public ModelAndView getNoDoctorsFoundView() {
        return super.view("user/noDoctorsFound");
    }


    @GetMapping("/profile/{id}")
    public ModelAndView getProfileView(@PathVariable String id, ModelAndView modelAndView) {
        modelAndView.addObject("doctor", this.doctorService.getById(id));
        return super.view("doctor/profile", modelAndView);
    }

    @GetMapping("/dashboard/{id}")
    public ModelAndView getDashboardDoctorView(@PathVariable String id, ModelAndView modelAndView) {
        DoctorServiceModel doctor = this.doctorService.getById(id);
        List<ConsultationServiceModel> consultations = doctor.getConsultations();
        modelAndView.addObject("consultations", consultations);
        return super.view("doctor/dashboard", modelAndView);
    }
}
