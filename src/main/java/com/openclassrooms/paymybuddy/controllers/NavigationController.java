package com.openclassrooms.paymybuddy.controllers;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@AllArgsConstructor
@Controller
public class NavigationController {
    @GetMapping("/transactions")
    public String transactionsPage() {
        return "transactions";
    }

    @GetMapping("/home")
    public String homePage() {
        return "home";
    }

    @GetMapping("/relation")
    public String relationPage() {
        return "relation";
    }
}
