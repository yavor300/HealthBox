package project.healthbox.web.controllers;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import project.healthbox.error.ObjectAlreadyExistsException;
import project.healthbox.error.ObjectNotFoundException;

@ControllerAdvice
public class GlobalExceptionHandlerController {
    @ExceptionHandler({ObjectNotFoundException.class})
    public ModelAndView handleObjectNotFoundException(ObjectNotFoundException e) {
        ModelAndView modelAndView = new ModelAndView("error/not-found");
        modelAndView.addObject("message", e.getMessage());
        return modelAndView;
    }

    @ExceptionHandler({ObjectAlreadyExistsException.class})
    public ModelAndView handleObjectAlreadyExistsException(ObjectAlreadyExistsException e) {
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("message", e.getMessage());
        return modelAndView;
    }

    @ExceptionHandler({AccessDeniedException.class})
    public ModelAndView handleAccessDeniedException(AccessDeniedException e) {
        ModelAndView modelAndView = new ModelAndView("error/access-denied");
        modelAndView.addObject("message", e.getMessage());
        return modelAndView;
    }
}