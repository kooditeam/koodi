package koodi.controller;

import java.util.ArrayList;
import koodi.domain.AnswerOption;
import koodi.domain.Question;
import koodi.repository.AnswerOptionRepository;
import koodi.service.QuestionSeriesService;
import koodi.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("tehtavat")
public class QuestionController {
    @Autowired
    private QuestionService questionService;
    
    @Autowired
    private QuestionSeriesService questionSeriesService;
    
    @Autowired
    private AnswerOptionRepository answerOptionRepository;
    
    @RequestMapping(method = RequestMethod.GET)
    public String list(Model model) {
        model.addAttribute("questions", questionService.findAll());
        model.addAttribute("allQuestionSeries", questionSeriesService.findAll());
        return "view_all_questions";
    }
    
    @RequestMapping(value="/{id}", method = RequestMethod.GET)
    public String list(Model model, @PathVariable Long id) {
        model.addAttribute("question", questionService.findById(id));
        return "view_question";
    }
    
    @RequestMapping(method = RequestMethod.POST)
    public String postAnswer(@ModelAttribute Question question,
            @RequestParam("correctAnswerOption") String correctAnswer,
            @RequestParam("falseAnswerOptions") String falseAnswers) {
 
        question.setAnswerOptions(new ArrayList<>());
        
        // creating false answer options
        String[] falseOptions = falseAnswers.split(";");
        for (String falseOption : falseOptions) {
            AnswerOption option = new AnswerOption();
            option.setAnswerText(falseOption.trim());
            option.setIsCorrect(false);
            answerOptionRepository.save(option);
            question.getAnswerOptions().add(option);
        }
        
        // creating the correct answer option
        AnswerOption correct = new AnswerOption();
        correct.setAnswerText(correctAnswer.trim());
        correct.setIsCorrect(true);
        answerOptionRepository.save(correct);
        question.getAnswerOptions().add(correct);
        
        questionService.save(question);
        
        return "redirect:/tehtavat";
    }
    
    @RequestMapping(value = "/{id}/poista", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public String delete(@PathVariable Long id){
        questionService.delete(id);
        return "{\"result\":\"success\"}";
    }
}
