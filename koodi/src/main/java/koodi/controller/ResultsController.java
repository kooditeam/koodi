package koodi.controller;

import java.util.List;
import koodi.domain.QuestionSeriesResult;
import koodi.service.ResultsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("tulokset")
public class ResultsController {
    
    @Autowired
    private ResultsService resultsService;
    
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    @RequestMapping(value = "/kayttajat/{id}", method = RequestMethod.GET)
    public String listUserResults(@PathVariable Long id, Model model){
        // check that current user can view the results for the requested user
        boolean isAllowed = resultsService.isAllowedToView(id);
        if(!isAllowed){
            model.addAttribute("error", "Sinulla ei ole oikeuksia katsella tämän käyttäjän tuloksia.");
            return "user_results";
        }
        
        List<QuestionSeriesResult> questionSets = resultsService.findAllResultsForUser(id);
        model.addAttribute("questionSets", questionSets);
        return "user_results";
    }    
}