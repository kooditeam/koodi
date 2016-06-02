package koodi.controller;

import javax.validation.Valid;
import koodi.domain.QuestionSeries;
import koodi.service.QuestionSeriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
@RequestMapping("topics")
public class QuestionSeriesController {
    
    @Autowired
    private QuestionSeriesService questionSeriesService;
    
    @Secured("ROLE_ADMIN")
    @RequestMapping(method = RequestMethod.GET)
    public String list(Model model) {
        model.addAttribute("activeQuestionSeries", questionSeriesService.findAll());
        model.addAttribute("archivedQuestionSeries", questionSeriesService.findRemoved());
        return "list_question_series";
    }
    
    @Secured("ROLE_ADMIN")
    @RequestMapping(method = RequestMethod.POST)
    public String postNewQuestionSeries(@Valid @ModelAttribute QuestionSeries questionSeries,
            BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            // error message
            return "redirect:/topics";
        }
        questionSeriesService.save(questionSeries);
        return "redirect:/topics";
    }
    
    @Secured("ROLE_ADMIN")
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
    
    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "/retrieve/{id}", method = RequestMethod.GET)
    public String retrieve(@PathVariable Long id) {
        QuestionSeries qs = questionSeriesService.findRemovedById(id);
        if(qs == null) {
            // error message
            return "redirect:/topics";
        }
        qs.setRemoved(false);
        questionSeriesService.save(qs);
        return "redirect:/topics";
    }
    
    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String edit(@PathVariable Long id, Model model) {
        QuestionSeries qs = questionSeriesService.findById(id);
        if (qs == null) {
            return "redirect:/topics";
        }
        model.addAttribute("questionSeries", qs);
        return "edit_question_series";
    }
    
    @Secured("ROLE_ADMIN")
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
