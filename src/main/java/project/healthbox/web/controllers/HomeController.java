package project.healthbox.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import project.healthbox.domain.models.binding.ChooseSpecialistBindingModel;
import project.healthbox.service.CityService;
import project.healthbox.service.SpecialtyService;
import project.healthbox.validation.doctor.FindDoctorValidator;


@Controller
public class HomeController extends BaseController {
    private final SpecialtyService specialtyService;
    private final CityService cityService;
    private final FindDoctorValidator findDoctorValidator;

    @Autowired
    public HomeController(SpecialtyService specialtyService, CityService cityService, FindDoctorValidator findDoctorValidator) {
        this.specialtyService = specialtyService;
        this.cityService = cityService;
        this.findDoctorValidator = findDoctorValidator;
    }

    @GetMapping("/")
    public ModelAndView getIndexView() {
        return super.view("index");
    }

    @GetMapping("/home")
    public ModelAndView getHomeView(ModelAndView modelAndView, @ModelAttribute(name = "model") ChooseSpecialistBindingModel model) {
        modelAndView.addObject("specialties", this.specialtyService.getAll());
        modelAndView.addObject("cities", this.cityService.getAll());
        modelAndView.addObject("model", model);
        return super.view("home", modelAndView);
    }

    @PostMapping("/home")
    public ModelAndView register(ModelAndView modelAndView, @ModelAttribute(name = "model") ChooseSpecialistBindingModel model, BindingResult bindingResult) {

        this.findDoctorValidator.validate(model, bindingResult);

        if (bindingResult.hasErrors()) {
            modelAndView.addObject("specialties", this.specialtyService.getAll());
            modelAndView.addObject("cities", this.cityService.getAll());
            modelAndView.addObject("model", model);
            return super.view("home", modelAndView);
        }

        String specialtyId = this.specialtyService.getIdBySpecialtyName(model.getSpecialty());
        String cityId = this.cityService.getIdByCityName(model.getLocation());

        return super.redirect("/doctor" + "/specialty_id=" + specialtyId + "&city_id=" + cityId + "&name=" + model.getDoctorName());
    }
}
