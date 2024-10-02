package ru.vovk.springboot.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.vovk.springboot.model.User;
import ru.vovk.springboot.service.UserService;

import java.security.Principal;

@Controller
@AllArgsConstructor
public class UserController {
    private UserService userService;

    @GetMapping
    public String indexPage() {
        return "redirect:/login";
    }

    @GetMapping("/user-page")
    public String getUser(Principal principal, Model model) {
        String name = principal.getName();
        User user = userService.getUserByUsername(name).orElseThrow();
        model.addAttribute("user", user);
        return "user-page";
    }
}
