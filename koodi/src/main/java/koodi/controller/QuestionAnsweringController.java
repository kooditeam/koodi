
package koodi.controller;

import java.util.List;
import koodi.domain.Answer;
import koodi.domain.AnswerOption;
import koodi.domain.Question;
import koodi.domain.TentativeAnswer;
import koodi.service.AnswerService;
import koodi.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("vastaukset")
public class QuestionAnsweringController {
    
    @Autowired
    private QuestionService questionService;    
    @Autowired
    private AnswerService answerService;
    
    @RequestMapping(method = RequestMethod.GET)
    public String list(Model model) {
        model.addAttribute("questions", questionService.findAll());
        return "answer_questions";
    }
    
    @RequestMapping(method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    @ResponseBody
    public String postAnswer(@RequestBody TentativeAnswer tentativeAnswer) {
        Answer answer = new Answer();
        Long questionId = new Long(tentativeAnswer.getQuestionId());
        Long answerOptionId = new Long(tentativeAnswer.getAnswerOptionId());
        List<AnswerOption> options = questionService.findById(questionId).getAnswerOptions();
        for(AnswerOption option : options){
            if(option.getId().equals(answerOptionId)){
                answer.setAnswerOption(option);
                break;
            }
        }        
        answer = answerService.save(answer);
        int result = 0;
        if(answer.getAnswerOption().getIsCorrect()){
            result = 1;
        }
        return "{\"result\": \"" + result + "\"}";
    }
}
