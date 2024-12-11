package com.openclassrooms.paymybuddy.controllers;

import com.openclassrooms.paymybuddy.models.User;
import com.openclassrooms.paymybuddy.services.IAuthService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@AllArgsConstructor
@Controller
public class AuthController {
    @Autowired
    private IAuthService authService;

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("user", new User());
        return "login";
    }

    @PostMapping("/home")
    public String auth(@RequestParam String email, @RequestParam String password, Model model) {
        User userActive = authService.auth(email, password);
        model.addAttribute("username", userActive.getUsername());
        model.addAttribute("email", userActive.getEmail());
        model.addAttribute("password", userActive.getPassword());
        if (userActive == null) {
            return "login";
        }
        return "home";
    }
}
