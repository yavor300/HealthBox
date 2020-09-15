package project.healthbox.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/consultation")
public class ConsultationController extends BaseController {
    @GetMapping("/send/{id}")
    public ModelAndView getSendConsultationView(@PathVariable String id, ModelAndView modelAndView) {
        modelAndView.addObject("doctorId", id);
        return super.view("consultation/sendConsultation", modelAndView);
    }
}
