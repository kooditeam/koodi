
package koodi.controller;

import koodi.domain.QuestionSeries;
import koodi.domain.TentativeAnswer;
import koodi.service.AnswerService;
import koodi.service.QuestionSeriesService;
import koodi.service.QuestionService;
import koodi.service.ResultsService;
import koodi.service.UserService;
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
    
    @Autowired
    private ResultsService resultsService;
    
    @Autowired
    private UserService userService;
    
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
        String rightOrNot = answerService.saveUsersAnswer(tentativeAnswer);
        return rightOrNot;
    }
    
    @RequestMapping(value="/topic/{id}/aiemmat", method = RequestMethod.GET, consumes = "application/json", produces = "application/json")
    @ResponseBody
    public String getPreviousResults(@PathVariable Long id){
        String resultSet = resultsService.getResultArray(userService.getCurrentUser().getId(), id);
        return resultSet;
    }
}
