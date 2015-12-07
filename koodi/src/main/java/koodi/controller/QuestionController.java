package koodi.controller;

import koodi.domain.Question;
import koodi.service.QuestionService;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
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

    @RequestMapping(method = RequestMethod.GET)
    public String list(Model model) {
        model.addAttribute("questions", questionService.findAll());
        return "view_all_questions";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String list(Model model, @PathVariable Long id) {
        model.addAttribute("question", questionService.findById(id));
        return "view_question";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String postAnswer(@ModelAttribute Question question,
            @RequestParam("answerOptionSet") String options) {
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

    @RequestMapping(value = "/{id}/poista", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public String delete(@PathVariable Long id) {
        questionService.delete(id);
        return "{\"result\":\"success\"}";
    }
}
