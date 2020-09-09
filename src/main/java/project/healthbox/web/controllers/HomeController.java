package project.healthbox.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

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
}
