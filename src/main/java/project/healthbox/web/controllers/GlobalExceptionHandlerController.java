package project.healthbox.web.controllers;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import project.healthbox.error.*;

@ControllerAdvice
public class GlobalExceptionHandlerController {
    @ExceptionHandler({ObjectNotFoundException.class})
    public ModelAndView handleObjectNotFoundException(ObjectNotFoundException e) {
        ModelAndView modelAndView = new ModelAndView("error/error");
        modelAndView.addObject("message", e.getMessage());
        return modelAndView;
    }

    @ExceptionHandler({ObjectAlreadyExistsException.class})
    public ModelAndView handleObjectAlreadyExistsException(ObjectAlreadyExistsException e) {
        ModelAndView modelAndView = new ModelAndView("error/error");
        modelAndView.addObject("message", e.getMessage());
        return modelAndView;
    }
}