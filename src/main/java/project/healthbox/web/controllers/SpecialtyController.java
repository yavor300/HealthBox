package project.healthbox.web.controllers;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import project.healthbox.domain.models.binding.SpecialtyAddBindingModel;
import project.healthbox.domain.models.view.SpecialtiesAllViewModel;
import project.healthbox.domain.models.view.SpecialtyDeleteViewModel;
import project.healthbox.error.CityAlreadyExistsException;
import project.healthbox.error.SpecialtyAlreadyExistsException;
import project.healthbox.service.SpecialtyService;
import project.healthbox.web.annotations.PageTitle;

import javax.validation.Valid;
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

    @ModelAttribute("specialtyAddBindingModel")
    public SpecialtyAddBindingModel specialtyAddBindingModel() {
        return new SpecialtyAddBindingModel();
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
    public ModelAndView createSpecialty(@Valid @ModelAttribute SpecialtyAddBindingModel specialtyAddBindingModel, BindingResult bindingResult,
                                        RedirectAttributes redirectAttributes,
                                        ModelAndView modelAndView) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("specialtyAddBindingModel", specialtyAddBindingModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.specialtyAddBindingModel", bindingResult);
            modelAndView.setViewName("redirect:/specialty/create");
            return modelAndView;
        }

        specialtyService.createSpecialty(specialtyAddBindingModel.getName());
        modelAndView.setViewName("redirect:/specialty/all");
        return modelAndView;
    }

    @ExceptionHandler({SpecialtyAlreadyExistsException.class})
    public ModelAndView handleSpecialtyAlreadyExistsException(SpecialtyAlreadyExistsException e) {
        ModelAndView modelAndView = new ModelAndView("error/error");
        modelAndView.addObject("message", e.getMessage());
        return modelAndView;
    }
}