package com.openclassrooms.paymybuddy.controllers;

import com.openclassrooms.paymybuddy.dto.UserDto;
import com.openclassrooms.paymybuddy.services.IUserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@AllArgsConstructor
@Controller
public class RegisterController {
    private IUserService userService;

    @GetMapping("/register")
    public String home(Model model){
        model.addAttribute("user", new UserDto());
        return "register";
    }

    @PostMapping("/submit")
    public String addUser(@ModelAttribute UserDto user, Model model) {
        try {
            this.userService.create(user);
            model.addAttribute("successMessage", "Votre compte a été créé avec succès ! <br> Vous pouvez vous connecter en cliquant sur ce <a href='login.html'>lien</a>");
            return "confirmation";
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "register";
        }
    }
}
