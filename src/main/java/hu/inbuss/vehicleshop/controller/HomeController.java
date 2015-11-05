package hu.inbuss.vehicleshop.controller;

import hu.inbuss.vehicleshop.model.Language;
import hu.inbuss.vehicleshop.util.Relatives;
import hu.inbuss.vehicleshop.util.Views;
import java.util.Arrays;
import java.util.List;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author Szecskó Zoltán <zoltan.szecsko@gmail.com>
 */
@Controller
public class HomeController {

    @RequestMapping(value = Relatives.HOME, method = RequestMethod.GET)
    public String home(final Model model) {
        final String username = SecurityContextHolder.getContext().getAuthentication().getName();
        model.addAttribute("username", username);
        return Views.HOME;
    }

    @ModelAttribute("languages")
    public List<Language> languages() {
        return Arrays.asList(Language.values());
    }
}
