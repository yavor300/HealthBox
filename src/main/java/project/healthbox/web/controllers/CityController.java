package project.healthbox.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import project.healthbox.domain.models.binding.CreateCityBindingModel;
import project.healthbox.domain.models.binding.UserRegisterBindingModel;
import project.healthbox.domain.models.service.CityServiceModel;
import project.healthbox.domain.models.service.DoctorServiceModel;
import project.healthbox.service.CityService;
import project.healthbox.web.annotations.PageTitle;

@Controller
@RequestMapping("/city")
public class CityController extends BaseController {
    private final CityService cityService;

    @Autowired
    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PageTitle("All Cities")
    public ModelAndView getAllView(ModelAndView modelAndView) {
        modelAndView.addObject("cities", this.cityService.getAll());
        return super.view("city/all-cities", modelAndView);
    }


    @GetMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PageTitle("Delete City")
    public ModelAndView deleteQuote(@PathVariable String id, ModelAndView modelAndView) {
        CityServiceModel city = this.cityService.getById(id);
        modelAndView.addObject("city", city);
        return super.view("city/delete-city", modelAndView);
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView deleteQuoteConfirm(@PathVariable String id) {
        this.cityService.deleteCity(id);
        return super.redirect("/city" + "/all");
    }

    @GetMapping("/create")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PageTitle("Add City")
    public ModelAndView createCity() {
        return super.view("city/create-city");
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView createCity(@ModelAttribute CreateCityBindingModel model) {
        this.cityService.createCity(model.getName());
        return super.redirect("/city" + "/all");
    }
}
