
package koodi.controller;

import koodi.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("tehtavat")
public class QuestionAnsweringController {
    
    @Autowired
    private QuestionRepository questionRepository;
    
    @RequestMapping(method = RequestMethod.GET)
    public String list(Model model) {
        model.addAttribute("questions", questionRepository.findAll());
        return "answer_questions";
    }
}
