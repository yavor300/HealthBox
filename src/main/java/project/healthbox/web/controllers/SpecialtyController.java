package project.healthbox.web.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import project.healthbox.domain.models.binding.SpecialtyAddBindingModel;
import project.healthbox.domain.models.service.SpecialtyServiceModel;
import project.healthbox.domain.models.view.SpecialtiesAllViewModel;
import project.healthbox.domain.models.view.SpecialtyDeleteViewModel;
import project.healthbox.service.SpecialtyService;
import project.healthbox.web.annotations.PageTitle;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/specialty")
public class SpecialtyController extends BaseController {
    private final SpecialtyService specialtyService;
    private final ModelMapper modelMapper;

    @Autowired
    public SpecialtyController(SpecialtyService specialtyService, ModelMapper modelMapper) {
        this.specialtyService = specialtyService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PageTitle("All Specialties")
    public ModelAndView getAllView(ModelAndView modelAndView) {
        List<SpecialtiesAllViewModel> specialties = this.specialtyService.getAll()
                .stream()
                .map(c -> this.modelMapper.map(c, SpecialtiesAllViewModel.class))
                .collect(Collectors.toList());
        modelAndView.addObject("specialties", specialties);
        return super.view("specialty/all-specialties", modelAndView);
    }

    @GetMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PageTitle("Delete Specialty")
    public ModelAndView deleteSpecialty(@PathVariable String id, ModelAndView modelAndView) {
        SpecialtyServiceModel specialtyServiceModel = this.specialtyService.getById(id);
        SpecialtyDeleteViewModel specialty = this.modelMapper.map(specialtyServiceModel, SpecialtyDeleteViewModel.class);
        modelAndView.addObject("specialty", specialty);
        return super.view("specialty/delete-specialty", modelAndView);
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView deleteSpecialtyConfirm(@PathVariable String id) {
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
    public ModelAndView createSpecialty(@ModelAttribute SpecialtyAddBindingModel model) {
        this.specialtyService.createSpecialty(model.getName());
        return super.redirect("/specialty" + "/all");
    }
}
