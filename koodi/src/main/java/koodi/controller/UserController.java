package koodi.controller;

import koodi.domain.User;
import koodi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("kayttajat")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @RequestMapping(method = RequestMethod.GET)
    public String list(Model model){
        model.addAttribute("users", userService.findAll());
        return "users";
    }
    
    @RequestMapping(value = "/lisaa", method = RequestMethod.GET)
    public String add(){
        return "add_user";
    }
    
    @RequestMapping(value = "/lisaa", method = RequestMethod.POST)
    public String create(User model){
        userService.save(model, new User());
        return "redirect:/users";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String show(@PathVariable Long id){
        return "";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
    public String update(@PathVariable Long id, Model model){
        return "";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public String delete(@PathVariable Long id){        
        return "redirect:/users";
    }
}
