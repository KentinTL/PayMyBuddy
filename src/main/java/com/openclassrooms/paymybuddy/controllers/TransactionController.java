package com.openclassrooms.paymybuddy.controllers;

import com.openclassrooms.paymybuddy.dto.TransferDto;
import com.openclassrooms.paymybuddy.services.*;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequestMapping("/transaction")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserInfos userInfos;

    @Autowired
    private IAccountBalanceService accountBalanceService;

    @GetMapping
    public String transactionPage(Model model) {
        try {
            var userId =  userInfos.getUserInfos().getId();

            model.addAttribute("relations", userService.getRelations(userId));
            model.addAttribute("transactions", transactionService.getTransactionsByUserId(userId));
            model.addAttribute("userId", userId);
            model.addAttribute("transferDto", new TransferDto());
            model.addAttribute("balance", accountBalanceService.getBalance(userId));
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", "Erreur lors du chargement des données : " + e.getMessage());
        }
        return "transaction";
    }

    @PostMapping
    public String makeTransaction(@Valid @ModelAttribute("transferDto") TransferDto transferDto, BindingResult bindingResult, Model model) {
        try {
            transactionService.makeTransfer(transferDto);
            model.addAttribute("successMessage", "Transaction effectuée avec succès !");
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", e.getMessage());
        }

        model.addAttribute("relations", userService.getRelations(transferDto.getUserId()));
        model.addAttribute("transactions", transactionService.getTransactionsByUserId(transferDto.getUserId()));
        model.addAttribute("userId", transferDto.getUserId());
        model.addAttribute("balance", accountBalanceService.getBalance(transferDto.getUserId()));

        return "transaction";
    }
}
