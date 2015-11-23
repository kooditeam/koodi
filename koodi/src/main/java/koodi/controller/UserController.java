package koodi.controller;

import javax.validation.Valid;
import koodi.domain.User;
import koodi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public String add(@ModelAttribute User user){
        return "add_user";
    }
    
    @RequestMapping(value = "/lisaa", method = RequestMethod.POST)
    public String create(
            @Valid @ModelAttribute User user,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes){
        if(bindingResult.hasErrors()){
            return "add_user";
        }
        userService.save(user);
        redirectAttributes.addFlashAttribute("message", "Uusi käyttäjä tallennettu!");
        return "redirect:/kayttajat";
    }
        
    @RequestMapping(value = "/rekisteroidy", method = RequestMethod.GET)
    public String showSignup(@ModelAttribute User user){
        return "register";
    }
    
    @RequestMapping(value = "/rekisteroidy", method = RequestMethod.POST)
    public String signup(
            @Valid @ModelAttribute User user,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes){
        if(bindingResult.hasErrors()){
            return "register";
        }
        user.setIsAdmin(false);
        userService.save(user);
        redirectAttributes.addFlashAttribute("message", "Tervetuloa käyttäjäksi!");
        return "redirect:/index";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String show(@PathVariable Long id){
        return "";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
    public String update(@PathVariable Long id, @ModelAttribute User user){
        
        return "redirect:/kayttajat";
    }
    
    @RequestMapping(value = "/{id}/poista", method = RequestMethod.POST)
    public String delete(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes){   
        userService.delete(id);
        redirectAttributes.addFlashAttribute("message", "Käyttäjä poistettu.");
        return "redirect:/kayttajat";
    }
}