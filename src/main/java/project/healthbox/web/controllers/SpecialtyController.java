package project.healthbox.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import project.healthbox.domain.models.binding.CreateCityBindingModel;
import project.healthbox.domain.models.binding.CreateSpecialtyBindingModel;
import project.healthbox.domain.models.service.CityServiceModel;
import project.healthbox.domain.models.service.SpecialtyServiceModel;
import project.healthbox.service.SpecialtyService;
import project.healthbox.web.annotations.PageTitle;

@Controller
@RequestMapping("/specialty")
public class SpecialtyController extends BaseController {
    private final SpecialtyService specialtyService;

    @Autowired
    public SpecialtyController(SpecialtyService specialtyService) {
        this.specialtyService = specialtyService;
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PageTitle("All Specialties")
    public ModelAndView getAllView(ModelAndView modelAndView) {
        modelAndView.addObject("specialties", this.specialtyService.getAll());
        return super.view("specialty/all-specialties", modelAndView);
    }


    @GetMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PageTitle("Delete Specialty")
    public ModelAndView deleteQuote(@PathVariable String id, ModelAndView modelAndView) {
        SpecialtyServiceModel specialty = this.specialtyService.getById(id);
        modelAndView.addObject("specialty", specialty);
        return super.view("specialty/delete-specialty", modelAndView);
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView deleteQuoteConfirm(@PathVariable String id) {
        this.specialtyService.deleteSpecialty(id);
        return super.redirect("/specialty" + "/all");
    }

    @GetMapping("/create")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PageTitle("Add Specialty")
    public ModelAndView createSpecialty() {
        return super.view("specialty/create-specialty");
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView createSpecialty(@ModelAttribute CreateSpecialtyBindingModel model) {
        this.specialtyService.createSpecialty(model.getName());
        return super.redirect("/specialty" + "/all");
    }
}
