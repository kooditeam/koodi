package koodi.controller;

import koodi.domain.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class IndexController {
    @RequestMapping("*")
    public String hello(Model model) {
        model.addAttribute("hello", "world");
        return "index";
    }
    
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String showLogin(){
        return "login";
    }
}
