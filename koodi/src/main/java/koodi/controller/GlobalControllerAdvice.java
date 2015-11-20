package koodi.controller;

import koodi.service.QuestionSeriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {
    
    @Autowired
    private QuestionSeriesService questionSeriesService;
    
    @ModelAttribute
    public void globalAttributes(Model model) {
        model.addAttribute("allQuestionSeries", questionSeriesService.findAll());
    }
}
