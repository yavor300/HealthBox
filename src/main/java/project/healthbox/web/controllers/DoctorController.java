package project.healthbox.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import project.healthbox.domain.models.binding.DoctorUpdateBindingModel;
import project.healthbox.service.DoctorService;
import project.healthbox.service.SpecialtyService;

@Controller
@RequestMapping("/doctor")
public class DoctorController extends BaseController {
    private final DoctorService doctorService;
    private final SpecialtyService specialtyService;

    @Autowired
    public DoctorController(DoctorService doctorService, SpecialtyService specialtyService) {
        this.doctorService = doctorService;
        this.specialtyService = specialtyService;
    }

    @GetMapping("/complete/{id}")
    public ModelAndView getRegisterView(@PathVariable String id, ModelAndView modelAndView) {
        modelAndView.addObject("doctorId", id);
        modelAndView.addObject("specialties", this.specialtyService.getAll());
        return super.view("user/doctorUpdate", modelAndView);
    }

    @PostMapping("/complete/{id}")
    public ModelAndView updateProfile(@PathVariable String id, @ModelAttribute DoctorUpdateBindingModel doctorUpdateBindingModel) {
        doctorUpdateBindingModel.setId(id);
        this.doctorService.update(doctorUpdateBindingModel);
        return super.redirect("/doctor" + "/home");
    }

    @GetMapping("/home")
    public ModelAndView getRegisterView() {
        return super.view("doctor/home");
    }
}
