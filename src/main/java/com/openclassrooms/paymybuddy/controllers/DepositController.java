package com.openclassrooms.paymybuddy.controllers;

import com.openclassrooms.paymybuddy.dto.DepositDto;
import com.openclassrooms.paymybuddy.services.IAccountBalanceService;
import com.openclassrooms.paymybuddy.services.TransactionService;
import com.openclassrooms.paymybuddy.services.UserInfos;
import com.openclassrooms.paymybuddy.services.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequestMapping("/deposit")
public class DepositController {

    @Autowired
    private UserInfos userInfos;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private UserService userService;

    @Autowired
    private IAccountBalanceService accountBalanceService;

    @GetMapping
    public String depositPage(Model model) {
        try {
            var userActive = userInfos.getUserInfos();

            model.addAttribute("transactions", transactionService.getDepositByUserid(userActive.getId()));
            model.addAttribute("userId", userActive.getId());
            model.addAttribute("depositDto", new DepositDto());
            model.addAttribute("balance", accountBalanceService.getBalance(userActive.getId()));
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", "Erreur lors du chargement des données : " + e.getMessage());
        }
        return "deposit";
    }

    @PostMapping
    public String makeDeposit(@Valid @ModelAttribute("depositDto") DepositDto depositDto, BindingResult bindingResult, Model model) {
        try {
            if (bindingResult.hasErrors()){
                return "deposit";
            }
            transactionService.makeDeposit(depositDto);
            model.addAttribute("successMessage", "Dépot effectuée avec succès !");
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", e.getMessage());
        }

        model.addAttribute("transactions", transactionService.getDepositByUserid(depositDto.getUserId()));
        model.addAttribute("userId", depositDto.getUserId());
        model.addAttribute("balance", accountBalanceService.getBalance(depositDto.getUserId()));

        return "deposit";
    }
}
