package koodi.controller;

import javax.validation.Valid;
import koodi.domain.QuestionSeries;
import koodi.service.QuestionSeriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
            // error message
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
            // error message
            return "redirect:/topics";
        }
        qs.setRemoved(false);
        questionSeriesService.save(qs);
        return "redirect:/topics";
    }
    
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String edit(@PathVariable Long id, Model model) {
        QuestionSeries qs = questionSeriesService.findById(id);
        if(qs == null) {
            // error message
            return "redirect:/topics/edit";
        }
        model.addAttribute("questionSeries", qs);
        return "edit_question_series";
    }
    
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.POST)
    public String postEdited(@PathVariable Long id, 
        @Valid @ModelAttribute QuestionSeries questionSeries,
        BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            // error message
            return "redirect:/edit/" + id;
        }
        questionSeriesService.save(questionSeries);
        return "redirect:/topics";
    }
}
