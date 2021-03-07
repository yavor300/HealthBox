package project.healthbox.web.controllers;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import project.healthbox.domain.models.binding.SpecialtyAddBindingModel;
import project.healthbox.domain.models.view.SpecialtiesAllViewModel;
import project.healthbox.domain.models.view.SpecialtyDeleteViewModel;
import project.healthbox.service.SpecialtyService;
import project.healthbox.web.annotations.PageTitle;

import java.util.stream.Collectors;

@Controller
@RequestMapping("/specialty")
@AllArgsConstructor
public class SpecialtyController {
    private final SpecialtyService specialtyService;
    private final ModelMapper modelMapper;

    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PageTitle("All Specialties")
    public ModelAndView getAllView(ModelAndView modelAndView) {
        modelAndView.addObject("specialties", specialtyService.getAll()
                .stream()
                .map(c -> modelMapper.map(c, SpecialtiesAllViewModel.class))
                .collect(Collectors.toList()));
        modelAndView.setViewName("specialty/all-specialties");
        return modelAndView;
    }

    @GetMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PageTitle("Delete Specialty")
    public ModelAndView deleteSpecialty(@PathVariable String id, ModelAndView modelAndView) {
        modelAndView.addObject("specialty", modelMapper.map(specialtyService.getById(id),
                SpecialtyDeleteViewModel.class));
        modelAndView.setViewName("specialty/delete-specialty");
        return modelAndView;
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView deleteSpecialtyConfirm(@PathVariable String id, ModelAndView modelAndView) {
        specialtyService.deleteSpecialty(id);
        modelAndView.setViewName("redirect:/specialty/all");
        return modelAndView;
    }

    @GetMapping("/create")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PageTitle("Add Specialty")
    public ModelAndView createSpecialty(ModelAndView modelAndView) {
        modelAndView.setViewName("specialty/create-specialty");
        return modelAndView;
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView createSpecialty(@ModelAttribute SpecialtyAddBindingModel model, ModelAndView modelAndView) {
        specialtyService.createSpecialty(model.getName());
        //TODO VALIDATE
        modelAndView.setViewName("redirect:/specialty/all");
        return modelAndView;
    }
}