
package koodi.controller;

import koodi.domain.AnswerOption;
import koodi.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("tehtavat")
public class QuestionAnsweringController {
    
    @Autowired
    private QuestionService questionService;
    
    @RequestMapping(method = RequestMethod.GET)
    public String list(Model model) {
        model.addAttribute("questions", questionService.findAll());
        return "answer_questions";
    }
    
    @RequestMapping(method = RequestMethod.POST)
    public String postAnswer(@ModelAttribute AnswerOption answerOption) {
        System.out.println("The answer was: " + answerOption.getAnswerText());
        
        return "redirect:/tehtavat";
    }
}
