package koodi.controller;

import javax.validation.Valid;
import koodi.domain.User;
import koodi.service.UserService;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "kayttajat", method = RequestMethod.GET)
    public String list(Model model) {
        model.addAttribute("users", userService.findAll());
        return "users";
    }

    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "kayttajat/lisaa", method = RequestMethod.GET)
    public String add(@ModelAttribute User user) {
        return "add_user";
    }

    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "kayttajat/lisaa", method = RequestMethod.POST)
    public String create(
            @Valid @ModelAttribute User user,
            @RequestParam String password2,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "add_user";
        }
        if (!user.getPassword().equals(password2)) {
            bindingResult.rejectValue("password", "error.user", "Salasanat eivät täsmää.");
            return "register";
        }
        if (userService.findByUsername(user.getUsername()) != null) {
            bindingResult.rejectValue("username", "error.user", "Käyttäjänimi on varattu - valitse toinen.");
            return "register";
        }
        userService.save(user);
        redirectAttributes.addFlashAttribute("message", "Uusi käyttäjä tallennettu!");
        return "redirect:/kayttajat";
    }

    @RequestMapping(value = "/rekisteroidy", method = RequestMethod.GET)
    public String showSignup(@ModelAttribute User user) {
        return "register";
    }

    @RequestMapping(value = "/rekisteroidy", method = RequestMethod.POST)
    public String signup(
            @Valid @ModelAttribute User user,
            BindingResult bindingResult,
            @RequestParam String password2,
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "register";
        }
        if (!user.getPassword().equals(password2)) {
            bindingResult.rejectValue("password", "error.user", "Salasanat eivät täsmää.");
            return "register";
        }
        if (userService.findByUsername(user.getUsername()) != null) {
            bindingResult.rejectValue("username", "error.user", "Käyttäjänimi on varattu - valitse toinen.");
            return "register";
        }
        user.setIsAdmin(false);
        userService.save(user);
        redirectAttributes.addFlashAttribute("message", "Tervetuloa käyttäjäksi!");
        return "redirect:/login";
    }

    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "kayttajat/{id}", method = RequestMethod.GET)
    public String show(@PathVariable Long id) {
        return "";
    }

    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "kayttajat/{id}", method = RequestMethod.POST)
    public String update(@PathVariable Long id, @ModelAttribute User user) {

        return "redirect:/kayttajat";
    }

    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "kayttajat/{id}/poista", method = RequestMethod.POST)
    public String delete(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {
        userService.delete(id);
        redirectAttributes.addFlashAttribute("message", "Käyttäjä poistettu.");
        return "redirect:/kayttajat";
    }
}
