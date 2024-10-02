package ru.vovk.springboot.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.vovk.springboot.model.Role;
import ru.vovk.springboot.model.User;
import ru.vovk.springboot.service.RoleService;
import ru.vovk.springboot.service.UserService;

import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@AllArgsConstructor
public class AdminController {
    private static final String REDIRECT = "redirect:/admin-page";
    private UserService userService;
    private RoleService roleService;

    @GetMapping("/admin-page")
    public String getAllUsers(Principal principal, Model model) {
        String name = principal.getName();
        User user = userService.getUserByUsername(name).orElseThrow();
        model.addAttribute("user", user);
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "admin-page";
    }

    @GetMapping("/add-new-user")
    public String getAddNewUserForm(Model model) {
        List<Role> roles = roleService.getAllRoles();
        model.addAttribute("user", new User());
        model.addAttribute("roles", roles);
        return "add-new-user";
    }

    @PostMapping("/add-new-user")
    public String addNewUserForm(User newUser) {
        userService.saveUser(newUser);
        return REDIRECT;
    }

    @GetMapping("/edit-form")
    public String updateUserForm(@RequestParam("id") Long id, Model model) {
        User user = userService.getUserById(id).orElseThrow();
        List<Role> roles = roleService.getAllRoles();
        model.addAttribute("user", user);
        model.addAttribute("roles", roles);
        return "edit-form";
    }

    @PostMapping("/edit-form")
    public String editUser(
            @RequestParam("id") Long id,
            @RequestParam("username") String username,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam("roles") Set<Role> roles,
            Model model) {
        List<Role> showRoles = roleService.getAllRoles();
        model.addAttribute("roles", showRoles);
        User user = userService.getUserById(id).orElseThrow();
        userService.updateUser(id, username, email, password, roles);
        return REDIRECT;
    }
//
//    @PostMapping("/edit-form")
//    public String updateUser(@RequestParam("id") Long id,
//                             User user) {
//        userService.updateUser(id,
//                user.getUsername(),
//                user.getEmail(),
//                user.getPassword(),
//                user.getRoles());
//        return REDIRECT;
//    }


    @GetMapping("/delete")
    public String deleteUser(@RequestParam("id") Long id, Model model) {
        User user = userService.getUserById(id).orElseThrow();
        Set<Role> roles = user.getRoles();
        model.addAttribute("user", user);
        model.addAttribute("roles", roles);
        userService.deleteUser(id);
        return REDIRECT;
    }
}
