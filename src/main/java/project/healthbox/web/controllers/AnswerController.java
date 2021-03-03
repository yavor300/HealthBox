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
        modelAndView.addObject("consultation", modelMapper.map(consultationService.getById(id),
                ConsultationDetailsViewModel.class));
        modelAndView.setViewName("consultation/answer");
        return modelAndView;
    }

    @PostMapping("/answer/{id}")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView answerConsultation(@Valid @ModelAttribute AnswerSendBindingModel answerSendBindingModel,
                                           BindingResult bindingResult,
                                           RedirectAttributes redirectAttributes,
                                           @PathVariable String id,
                                           ModelAndView modelAndView) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("answerSendBindingModel", answerSendBindingModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.answerSendBindingModel", bindingResult);
            modelAndView.setViewName("redirect:/consultation/answer/" + id);
            return modelAndView;
        }

        AnswerServiceModel answerServiceModel = answerService.save(modelMapper.map(answerSendBindingModel, AnswerServiceModel.class));
        ConsultationServiceModel consultationServiceModel = consultationService.getById(id);
        consultationService.setAnswer(consultationServiceModel, answerServiceModel);

        modelAndView.setViewName("redirect:/doctor/dashboard");
        return modelAndView;
    }
}