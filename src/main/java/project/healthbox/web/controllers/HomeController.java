package project.healthbox.web.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import project.healthbox.domain.models.binding.ChooseSpecialistBindingModel;
import project.healthbox.domain.models.view.CitySearchViewModel;
import project.healthbox.domain.models.view.SpecialtySearchViewModel;
import project.healthbox.service.CityService;
import project.healthbox.service.SpecialtyService;
import project.healthbox.service.UserService;
import project.healthbox.validation.doctor.FindDoctorValidator;
import project.healthbox.web.annotations.PageTitle;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;


@Controller
public class HomeController {
    private final SpecialtyService specialtyService;
    private final CityService cityService;
    private final FindDoctorValidator findDoctorValidator;
    private final UserService userService;
    private final ModelMapper modelMapper;

    @Autowired
    public HomeController(SpecialtyService specialtyService, CityService cityService, FindDoctorValidator findDoctorValidator, UserService userService, ModelMapper modelMapper) {
        this.specialtyService = specialtyService;
        this.cityService = cityService;
        this.findDoctorValidator = findDoctorValidator;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/")
    @PreAuthorize("isAnonymous()")
    @PageTitle("Index")
    public ModelAndView getIndexView(ModelAndView modelAndView) {
        modelAndView.setViewName("index");
        return modelAndView;
        //return super.view("index");
    }

    @GetMapping("/home")
    @PreAuthorize("isAuthenticated()")
    @PageTitle("Home")
    public ModelAndView getHomeView(ModelAndView modelAndView, Principal principal) {
        List<SpecialtySearchViewModel> specialties = this.specialtyService.getAll()
                .stream()
                .map(s -> this.modelMapper.map(s, SpecialtySearchViewModel.class))
                .collect(Collectors.toList());

        List<CitySearchViewModel> cities = this.cityService.getAll()
                .stream()
                .map(c -> this.modelMapper.map(c, CitySearchViewModel.class))
                .collect(Collectors.toList());

        String userId = this.userService.getByEmail(principal.getName()).getId();
        modelAndView.addObject("specialties", specialties);
        modelAndView.addObject("cities", cities);
        modelAndView.addObject("userId", userId);

        modelAndView.setViewName("home");
        return modelAndView;
        //return super.view("home", modelAndView);
    }

//    @PostMapping("/home")
//    @PreAuthorize("isAuthenticated()")
//    public ModelAndView register(ModelAndView modelAndView, @ModelAttribute(name = "model") ChooseSpecialistBindingModel model, BindingResult bindingResult, Principal principal) {
//        List<SpecialtySearchViewModel> specialties = this.specialtyService.getAll()
//                .stream()
//                .map(s -> this.modelMapper.map(s, SpecialtySearchViewModel.class))
//                .collect(Collectors.toList());
//
//        List<CitySearchViewModel> cities = this.cityService.getAll()
//                .stream()
//                .map(c -> this.modelMapper.map(c, CitySearchViewModel.class))
//                .collect(Collectors.toList());
//
//        this.findDoctorValidator.validate(model, bindingResult);
//
//        if (bindingResult.hasErrors()) {
//            modelAndView.addObject("specialties", specialties);
//            modelAndView.addObject("cities", cities);
//            modelAndView.addObject("userId", this.userService.getByEmail(principal.getName()).getId());
//            modelAndView.addObject("model", model);
//            return super.view("home", modelAndView);
//        }
//
//        String specialtyId = this.specialtyService.getIdBySpecialtyName(model.getSpecialty());
//        String cityId = this.cityService.getIdByCityName(model.getLocation());
//
//        return super.redirect("/doctor" + "/specialty_id=" + specialtyId + "&city_id=" + cityId + "&name=" + model.getDoctorName());
//    }
}
