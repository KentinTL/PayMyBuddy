package com.openclassrooms.paymybuddy.controllers;

import com.openclassrooms.paymybuddy.models.User;
import com.openclassrooms.paymybuddy.services.IAccountBalanceService;
import com.openclassrooms.paymybuddy.services.IUserService;
import com.openclassrooms.paymybuddy.services.UserInfos;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@AllArgsConstructor
@Controller
public class AuthController {

    @Autowired
    private UserInfos userInfos;

    @Autowired
    private IAccountBalanceService accountBalanceService;

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("user", new User());
        return "login";
    }

    @GetMapping("/home")
    public String getProfile(Model model) {
        var userActive = userInfos.getUserInfos();
        model.addAttribute("username", userActive.getUsername());
        model.addAttribute("email", userActive.getEmail());
        model.addAttribute("password", userActive.getPassword());
        model.addAttribute("balance", accountBalanceService.getBalance(userActive.getId()));
        return "home";
    }

}
