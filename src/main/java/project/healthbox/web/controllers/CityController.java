package project.healthbox.web.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import project.healthbox.domain.models.binding.CreateCityBindingModel;
import project.healthbox.domain.models.service.CityServiceModel;
import project.healthbox.domain.models.view.CityViewModel;
import project.healthbox.service.CityService;
import project.healthbox.web.annotations.PageTitle;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/city")
public class CityController extends BaseController {
    private final CityService cityService;
    private final ModelMapper modelMapper;

    @Autowired
    public CityController(CityService cityService, ModelMapper modelMapper) {
        this.cityService = cityService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PageTitle("All Cities")
    public ModelAndView getAllView(ModelAndView modelAndView) {
        List<CityViewModel> cities = this.cityService.getAll()
                .stream()
                .map(c -> this.modelMapper.map(c, CityViewModel.class))
                .collect(Collectors.toList());
        modelAndView.addObject("cities", cities);
        return super.view("city/all-cities", modelAndView);
    }


    @GetMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PageTitle("Delete City")
    public ModelAndView deleteCity(@PathVariable String id, ModelAndView modelAndView) {
        CityServiceModel cityServiceModel = this.cityService.getById(id);
        CityViewModel city = this.modelMapper.map(cityServiceModel, CityViewModel.class);
        modelAndView.addObject("city", city);
        return super.view("city/delete-city", modelAndView);
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView deleteCityConfirm(@PathVariable String id) {
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
