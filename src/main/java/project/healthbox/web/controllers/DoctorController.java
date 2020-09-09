package project.healthbox.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import project.healthbox.domain.models.binding.DoctorUpdateBindingModel;
import project.healthbox.service.DoctorService;

@Controller
@RequestMapping("/doctor")
public class DoctorController extends BaseController {
    private final DoctorService doctorService;

    @Autowired
    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @GetMapping("/complete/{id}")
    public ModelAndView getRegisterView(@PathVariable String id, ModelAndView modelAndView) {
        modelAndView.addObject("doctorId", id);
        return super.view("user/doctorUpdate", modelAndView);
    }

    @PostMapping("/complete/{id}")
    public ModelAndView updateProfile(@PathVariable String id, @ModelAttribute DoctorUpdateBindingModel doctorUpdateBindingModel) {
        doctorUpdateBindingModel.setId(id);
        this.doctorService.update(doctorUpdateBindingModel);
        return super.view("doctor/home");
    }
}
