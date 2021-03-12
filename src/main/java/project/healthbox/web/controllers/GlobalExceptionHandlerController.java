package project.healthbox.web.controllers;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import project.healthbox.error.CityNotFoundException;
import project.healthbox.error.ConsultationNotFoundException;
import project.healthbox.error.DoctorNotFoundException;
import project.healthbox.error.SpecialtyNotFoundException;

@ControllerAdvice
public class GlobalExceptionHandlerController {
    @ExceptionHandler({CityNotFoundException.class})
    public ModelAndView handleCityNotFoundException(CityNotFoundException e) {
        ModelAndView modelAndView = new ModelAndView("error/error");
        modelAndView.addObject("message", e.getMessage());
        return modelAndView;
    }

    @ExceptionHandler({DoctorNotFoundException.class})
    public ModelAndView handleDoctorNotFoundException(DoctorNotFoundException e) {
        ModelAndView modelAndView = new ModelAndView("error/error");
        modelAndView.addObject("message", e.getMessage());
        return modelAndView;
    }

    @ExceptionHandler({ConsultationNotFoundException.class})
    public ModelAndView handleConsultationNotFoundException(ConsultationNotFoundException e) {
        ModelAndView modelAndView = new ModelAndView("error/error");
        modelAndView.addObject("message", e.getMessage());
        return modelAndView;
    }

    @ExceptionHandler({SpecialtyNotFoundException.class})
    public ModelAndView handleSpecialtyNotFoundException(SpecialtyNotFoundException e) {
        ModelAndView modelAndView = new ModelAndView("error/error");
        modelAndView.addObject("message", e.getMessage());
        return modelAndView;
    }
}