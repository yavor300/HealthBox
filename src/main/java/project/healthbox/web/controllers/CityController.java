package project.healthbox.web.controllers;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import project.healthbox.domain.models.binding.CityAddBindingModel;
import project.healthbox.domain.models.view.CitiesAllViewModel;
import project.healthbox.domain.models.view.CityDeleteViewModel;
import project.healthbox.service.CityService;
import project.healthbox.web.annotations.PageTitle;

import javax.validation.Valid;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/city")
@AllArgsConstructor
public class CityController {
    private final CityService cityService;
    private final ModelMapper modelMapper;

    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PageTitle("All Cities")
    public ModelAndView getAllView(ModelAndView modelAndView) {
        modelAndView.addObject("cities", cityService.getAll()
                .stream()
                .map(cityServiceModel -> modelMapper.map(cityServiceModel, CitiesAllViewModel.class))
                .collect(Collectors.toList()));
        modelAndView.setViewName("city/all-cities");
        return modelAndView;
    }

    @GetMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PageTitle("Delete City")
    public ModelAndView deleteCity(@PathVariable String id, ModelAndView modelAndView) {
        modelAndView.addObject("city", modelMapper.map(cityService.getById(id), CityDeleteViewModel.class));
        modelAndView.setViewName("city/delete-city");
        return modelAndView;
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView deleteCityConfirm(@PathVariable String id, ModelAndView modelAndView) {
        cityService.deleteCity(id);
        modelAndView.setViewName("redirect:/city/all");
        return modelAndView;
    }

    @GetMapping("/create")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PageTitle("Add City")
    public ModelAndView createCity(ModelAndView modelAndView) {
        modelAndView.setViewName("city/create-city");
        return modelAndView;
    }

    @ModelAttribute("cityAddBindingModel")
    public CityAddBindingModel cityAddBindingModel() {
        return new CityAddBindingModel();
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView createCity(@Valid @ModelAttribute CityAddBindingModel cityAddBindingModel, BindingResult bindingResult,
                                   RedirectAttributes redirectAttributes,
                                   ModelAndView modelAndView) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("cityAddBindingModel", cityAddBindingModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.cityAddBindingModel", bindingResult);
            modelAndView.setViewName("redirect:/city/create");
            return modelAndView;
        }

        cityService.createCity(cityAddBindingModel.getName());
        modelAndView.setViewName("redirect:/city/all");
        return modelAndView;
    }
}