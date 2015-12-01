package koodi.controller;

import koodi.domain.QuestionSeries;
import koodi.service.QuestionSeriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
@RequestMapping("topics")
public class QuestionSeriesController {
    
    @Autowired
    private QuestionSeriesService questionSeriesService;
    
    @RequestMapping(method = RequestMethod.GET)
    public String list(Model model) {
        model.addAttribute("allQuestionSeries", questionSeriesService.findAll());
        return "list_question_series";
    }
    
    @RequestMapping(method = RequestMethod.POST)
    public String postNewQuestionSeries(@ModelAttribute QuestionSeries questionSeries) {
        questionSeriesService.save(questionSeries);
        return "redirect:/topics";
    }
}
