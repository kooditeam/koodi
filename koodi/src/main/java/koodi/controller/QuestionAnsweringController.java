
package koodi.controller;

import java.util.List;
import koodi.domain.Answer;
import koodi.domain.AnswerOption;
import koodi.domain.QuestionSeries;
import koodi.domain.TentativeAnswer;
import koodi.service.AnswerService;
import koodi.service.QuestionSeriesService;
import koodi.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("vastaukset")
public class QuestionAnsweringController {
    
    @Autowired
    private QuestionService questionService;
    
    @Autowired
    private QuestionSeriesService questionSeriesService; 
    
    @Autowired
    private AnswerService answerService;
    
    @RequestMapping(value="/topic/{id}", method = RequestMethod.GET)
    public String list(Model model, @PathVariable Long id) {
        QuestionSeries qs = questionSeriesService.findById(id);
        model.addAttribute("questions", questionService.findByQuestionSeries(qs));
        model.addAttribute("questionSeries", qs);
        return "answer_questions";
    }
    
    @RequestMapping(method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    @ResponseBody
    public String saveAnswer(@RequestBody TentativeAnswer tentativeAnswer) {
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
