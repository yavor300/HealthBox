package project.healthbox.web.controllers;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import project.healthbox.domain.models.binding.AnswerSendBindingModel;
import project.healthbox.domain.models.service.AnswerServiceModel;
import project.healthbox.domain.models.service.ConsultationServiceModel;
import project.healthbox.domain.models.view.ConsultationDetailsViewModel;
import project.healthbox.service.AnswerService;
import project.healthbox.service.ConsultationService;
import project.healthbox.web.annotations.PageTitle;

import javax.validation.Valid;

@Controller
@RequestMapping("/consultation")
@AllArgsConstructor
public class AnswerController {
    private final AnswerService answerService;
    //private final AnswerValidator answerValidator;
    private final ConsultationService consultationService;
    private final ModelMapper modelMapper;

    @ModelAttribute("answerSendBindingModel")
    public AnswerSendBindingModel answerSendBindingModel() {
        return new AnswerSendBindingModel();
    }

    @GetMapping("/answer/{id}")
    @PreAuthorize("isAuthenticated()")
    @PageTitle("Answer Consultation")
    public ModelAndView getConsultationAnswerView(@PathVariable String id, ModelAndView modelAndView) {
        ConsultationServiceModel consultationServiceModel = consultationService.getById(id);
        ConsultationDetailsViewModel consultation = modelMapper.map(consultationServiceModel, ConsultationDetailsViewModel.class);
        modelAndView.addObject("consultation", consultation);
//        modelAndView.addObject("model", model);
        modelAndView.setViewName("consultation/answer");
        return modelAndView;
        //return super.view("consultation/answer", modelAndView);
    }

    @PostMapping("/answer/{id}")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView answerConsultation(@Valid @ModelAttribute AnswerSendBindingModel answerSendBindingModel,
                                           BindingResult bindingResult,
                                           RedirectAttributes redirectAttributes,
                                           @PathVariable String id,
                                           ModelAndView modelAndView) {

        //answerValidator.validate(model, bindingResult);

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("answerSendBindingModel", answerSendBindingModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.answerSendBindingModel", bindingResult);
            modelAndView.setViewName("redirect:/consultation/answer/" + id);
            return modelAndView;
//            ConsultationServiceModel consultationServiceModel = this.consultationService.getById(id);
//            ConsultationDetailsViewModel consultation = this.modelMapper.map(consultationServiceModel, ConsultationDetailsViewModel.class);
//            modelAndView.addObject("consultation", consultation);
//            modelAndView.addObject("model", model);
//            return super.view("consultation/answer", modelAndView);
        }

        AnswerServiceModel answerServiceModel = answerService.save(modelMapper.map(answerSendBindingModel, AnswerServiceModel.class));
        ConsultationServiceModel consultationServiceModel = consultationService.getById(id);

        consultationService.setAnswer(consultationServiceModel, answerServiceModel);

        modelAndView.setViewName("redirect:/doctor/dashboard");
        return modelAndView;
        //return super.redirect("/doctor" + "/dashboard");
    }
}
