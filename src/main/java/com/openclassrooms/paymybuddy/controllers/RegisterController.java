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
        this.userService.create(user);
        return "home";
    }
}
