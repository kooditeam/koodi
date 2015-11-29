package koodi.controller;

import koodi.domain.User;
import koodi.service.QuestionSeriesService;
import koodi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {
    
    @Autowired
    private QuestionSeriesService questionSeriesService;
    @Autowired
    private UserService userService;
    
    @ModelAttribute
    public void globalAttributes(Model model) {
        model.addAttribute("allQuestionSeries", questionSeriesService.findAll());
        User user = userService.getAuthenticatedUser();
        model.addAttribute("userId", user != null ? user.getId() : null); 
    }
}
