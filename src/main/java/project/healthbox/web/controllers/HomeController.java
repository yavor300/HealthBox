package project.healthbox.web.controllers;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import project.healthbox.domain.models.view.CitySearchViewModel;
import project.healthbox.domain.models.view.SpecialtySearchViewModel;
import project.healthbox.service.CityService;
import project.healthbox.service.SpecialtyService;
import project.healthbox.service.UserService;
import project.healthbox.web.annotations.PageTitle;

import java.security.Principal;
import java.util.stream.Collectors;


@Controller
@AllArgsConstructor
public class HomeController {
    private final SpecialtyService specialtyService;
    private final CityService cityService;
    private final UserService userService;
    private final ModelMapper modelMapper;

    @GetMapping("/")
    @PreAuthorize("isAnonymous()")
    @PageTitle("Index")
    public ModelAndView getIndexView(ModelAndView modelAndView) {
        modelAndView.setViewName("index");
        return modelAndView;
    }

    @GetMapping("/home")
    @PreAuthorize("isAuthenticated()")
    @PageTitle("Home")
    public ModelAndView getHomeView(ModelAndView modelAndView, Principal principal) {
        modelAndView.addObject("specialties", specialtyService.getAll()
                .stream()
                .map(specialtyServiceModel -> modelMapper.map(specialtyServiceModel, SpecialtySearchViewModel.class))
                .collect(Collectors.toList()));

        modelAndView.addObject("cities", cityService.getAll()
                .stream()
                .map(cityServiceModel -> modelMapper.map(cityServiceModel, CitySearchViewModel.class))
                .collect(Collectors.toList()));

        modelAndView.addObject("userId", userService.getByEmail(principal.getName()).getId());

        modelAndView.setViewName("home");
        return modelAndView;
    }
}
