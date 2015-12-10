package koodi.controller;

import javax.validation.Valid;
import koodi.domain.Question;
import koodi.service.QuestionService;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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

    @Secured("ROLE_ADMIN")
    @RequestMapping(method = RequestMethod.GET)
    public String list(Model model) {
        model.addAttribute("questions", questionService.findAll());
        return "view_all_questions";
    }

    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String edit(Model model, @PathVariable Long id) {
        Question question = questionService.findById(id);
        if(question == null) {
            return "redirect:/tehtavat";
        }
        model.addAttribute("question", question);
        return "edit_question";
    }
    
    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.POST)
    public String postEdited(@PathVariable Long id,
            @Valid @ModelAttribute Question question,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "redirect:/tehtavat/" + id;
        }
        questionService.save(question);
        return "redirect:/tehtavat";
    }

    @Secured("ROLE_ADMIN")
    @RequestMapping(method = RequestMethod.POST)
    public String save(@RequestParam("answerOptionSet") String options,
            @Valid @ModelAttribute Question question,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // error message
            return "redirect:/tehtavat";
        }
        JSONArray optionsArray = null;
        try{
        JSONParser parser = new JSONParser();
        optionsArray = (JSONArray)parser.parse(options);
        
        } catch (Exception exc) {
            return "tehtavat";
        }        
        
        questionService.postNewExercise(question, optionsArray);
        return "redirect:/tehtavat";
    }

    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "/{id}/poista", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public String delete(@PathVariable Long id) {
        questionService.delete(id);
        return "{\"result\":\"success\"}";
    }
}
