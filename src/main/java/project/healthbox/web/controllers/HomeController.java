package project.healthbox.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import project.healthbox.domain.models.binding.ChooseSpecialistBindingModel;
import project.healthbox.domain.models.binding.UserRegisterBindingModel;
import project.healthbox.domain.models.service.UserServiceModel;

import javax.servlet.http.HttpSession;

@Controller
public class HomeController extends BaseController {
    @GetMapping("/")
    public ModelAndView getIndexView() {
        return super.view("index");
    }

    @GetMapping("/home")
    public ModelAndView getHomeView() {
        return super.view("home");
    }

    @PostMapping("/home")
    public ModelAndView register(@ModelAttribute ChooseSpecialistBindingModel chooseModel) {

        return super.redirect("/user" + "/login");
    }
}
