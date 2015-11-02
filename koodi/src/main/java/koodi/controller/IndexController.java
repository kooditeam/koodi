package koodi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {
    @RequestMapping("*")
    public String hello(Model model) {
        model.addAttribute("hello", "world");
        return "index";
    }
}
