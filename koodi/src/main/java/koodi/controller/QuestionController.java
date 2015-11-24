package koodi.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import koodi.domain.AnswerOption;
import koodi.domain.Question;
import koodi.repository.AnswerOptionRepository;
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
    private AnswerOptionRepository answerOptionRepository;
    
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
    public String postAnswer(@ModelAttribute Question question,
            @RequestParam("correctAnswerOption") String correctAnswer,
            @RequestParam("falseAnswerOptions") String falseAnswers) {
        question.setAnswerOptions(new ArrayList<>());
        ArrayList<AnswerOption> allOptions = new ArrayList<>();
        // creating false answer options
        String[] falseStrings = falseAnswers.split(";");
        for (String falseString : falseStrings) {
            AnswerOption falseOption = new AnswerOption();
            falseOption.setAnswerText(falseString.trim());
            falseOption.setIsCorrect(false);
            allOptions.add(falseOption);
        }
        // creating the correct answer option
        AnswerOption correct = new AnswerOption();
        correct.setAnswerText(correctAnswer.trim());
        correct.setIsCorrect(true);
        allOptions.add(correct);
        // save all options in random order
        long seed = System.nanoTime();
        Collections.shuffle(allOptions, new Random(seed));
        for (AnswerOption option : allOptions) {
            answerOptionRepository.save(option);
            question.getAnswerOptions().add(option);
        }
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
