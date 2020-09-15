package project.healthbox.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import project.healthbox.domain.models.binding.ChooseSpecialistBindingModel;
import project.healthbox.domain.models.binding.DoctorUpdateBindingModel;
import project.healthbox.domain.models.service.DoctorServiceModel;
import project.healthbox.service.DoctorService;
import project.healthbox.service.SpecialtyService;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class HomeController extends BaseController {
    private final DoctorService doctorService;
    private final SpecialtyService specialtyService;

    @Autowired
    public HomeController(DoctorService doctorService, SpecialtyService specialtyService) {
        this.doctorService = doctorService;
        this.specialtyService = specialtyService;
    }

    @GetMapping("/")
    public ModelAndView getIndexView() {
        return super.view("index");
    }

    @GetMapping("/home")
    public ModelAndView getHomeView(ModelAndView modelAndView) {
        modelAndView.addObject("specialties", this.specialtyService.getAll());
        return super.view("home", modelAndView);
    }

    @PostMapping("/home")
    public ModelAndView register(@ModelAttribute ChooseSpecialistBindingModel chooseModel, HttpSession session) {
        List<DoctorServiceModel> allByGivenCriteria = this.doctorService.findAllByGivenCriteria(chooseModel);
        if (allByGivenCriteria != null) {
            session.setAttribute("foundDoctors", allByGivenCriteria);
            return super.redirect("/user" + "/doctors");
        } else {
            return super.redirect("/user" + "/noDoctorsFound");
        }
    }
}
