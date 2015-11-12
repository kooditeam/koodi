
package koodi.controller;

import koodi.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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
}
