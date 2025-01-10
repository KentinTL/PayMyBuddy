package com.openclassrooms.paymybuddy.controllers;

import com.openclassrooms.paymybuddy.dto.TransferDto;
import com.openclassrooms.paymybuddy.services.AccountBalanceService;
import com.openclassrooms.paymybuddy.services.TransactionService;
import com.openclassrooms.paymybuddy.services.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@Slf4j
@Controller
@RequestMapping("/transaction")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private UserService userService;

    @GetMapping("/{userId}")
    public String transactionPage(@PathVariable Long userId, Model model) {
        try {
            // Charger les relations et les transactions
            model.addAttribute("relations", userService.getRelations(userId));
            model.addAttribute("transactions", transactionService.getTransactionsByUserId(userId));
            model.addAttribute("userId", userId);
            model.addAttribute("transferDo", new TransferDto());
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", "Erreur lors du chargement des données : " + e.getMessage());
        }
        return "transaction";
    }

    @PostMapping
    public String makeTransaction(@Valid @ModelAttribute("transferDto") TransferDto transferDto, Model model) {
        try {
            transactionService.makeTransfer(transferDto);
            model.addAttribute("successMessage", "Transaction effectuée avec succès !");
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", e.getMessage());
        }

        // Recharger les données pour l'affichage
        model.addAttribute("relations", userService.getRelations(transferDto.getUserId()));
        model.addAttribute("transactions", transactionService.getTransactionsByUserId(transferDto.getUserId()));
        model.addAttribute("userId", transferDto.getUserId());

        return "transaction";
    }
}
