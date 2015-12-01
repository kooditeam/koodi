package koodi.controller;

import koodi.domain.QuestionSeries;
import koodi.repository.QuestionRepository;
import koodi.service.QuestionSeriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/* 
    TODO: Check admin permissions
*/

@Controller
@RequestMapping("topics")
public class QuestionSeriesController {
    
    @Autowired
    private QuestionSeriesService questionSeriesService;
    
    @RequestMapping(method = RequestMethod.GET)
    public String list(Model model) {
        model.addAttribute("activeQuestionSeries", questionSeriesService.findAll());
        model.addAttribute("archivedQuestionSeries", questionSeriesService.findRemoved());
        return "list_question_series";
    }
    
    @RequestMapping(method = RequestMethod.POST)
    public String postNewQuestionSeries(@ModelAttribute QuestionSeries questionSeries) {
        questionSeriesService.save(questionSeries);
        return "redirect:/topics";
    }
    
    @RequestMapping(value = "/remove/{id}", method = RequestMethod.GET)
    public String remove(@PathVariable Long id) {
        QuestionSeries qs = questionSeriesService.findById(id);
        if(qs == null) {
            // raise errors
            return "redirect:/topics";
        }
        qs.setRemoved(true);
        questionSeriesService.save(qs);
        return "redirect:/topics";
    }
    
    @RequestMapping(value = "/retrieve/{id}", method = RequestMethod.GET)
    public String retrieve(@PathVariable Long id) {
        QuestionSeries qs = questionSeriesService.findById(id);
        if(qs == null) {
            // raise errors
            return "redirect:/topics";
        }
        qs.setRemoved(false);
        questionSeriesService.save(qs);
        return "redirect:/topics";
    }
}
