package com.openclassrooms.paymybuddy.controllers;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@AllArgsConstructor
@Controller
public class NavigationController {
    @GetMapping("/transaction")
    public String transactionsPage() {
        return "transaction/1";
    }

    @GetMapping("/home")
    public String homePage() {
        return "home/1";
    }

    @GetMapping("/relation")
    public String relationPage() {
        return "relation";
    }
}
