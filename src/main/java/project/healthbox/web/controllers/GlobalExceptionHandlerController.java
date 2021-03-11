package project.healthbox.web.controllers;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import project.healthbox.error.CityNotFoundException;

@ControllerAdvice
public class GlobalExceptionHandlerController {
    @ExceptionHandler({CityNotFoundException.class})
    public ModelAndView handleCityNotFoundException(CityNotFoundException e) {
        ModelAndView modelAndView = new ModelAndView("error/city-error");
        modelAndView.addObject("message", e.getMessage());
        return modelAndView;
    }
}
