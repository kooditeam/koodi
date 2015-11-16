package koodi.controller;

import koodi.domain.AnswerOption;
import koodi.domain.Question;
import koodi.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("tehtavat")
public class QuestionController {
    @Autowired
    private QuestionService questionService;
    
    @RequestMapping(method = RequestMethod.GET)
    public String list(Model model) {
        model.addAttribute("questions", questionService.findAll());
        return "view_all_questions";
    }
    
    @RequestMapping(value="/{id}", method = RequestMethod.GET)
    public String list(Model model, @PathVariable Long id) {
        model.addAttribute("question", questionService.findById(id));
        return "view_question";
    }
    
    @RequestMapping(method = RequestMethod.POST)
    public String postAnswer(@ModelAttribute Question question) {
        //System.out.println("The answer was: " + answerOption.getAnswerText());
        questionService.save(question);
        return "redirect:/tehtavat";
    }
    
}
