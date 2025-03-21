package com.openclassrooms.paymybuddy.controllers;

import com.openclassrooms.paymybuddy.dto.WithdrawalDto;
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
@RequestMapping("/withdrawal")
public class WithdrawalController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserInfos userInfos;

    @Autowired
    private IAccountBalanceService accountBalanceService;

    @GetMapping
    public String withdrawalPage(Model model) {
        try {
            var userActive = userInfos.getUserInfos();

            model.addAttribute("transactions", transactionService.getWithdrawalByUserid(userActive.getId()));
            model.addAttribute("userId", userActive.getId());
            model.addAttribute("withdrawalDto", new WithdrawalDto());
            model.addAttribute("balance", accountBalanceService.getBalance(userActive.getId()));
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", "Erreur lors du chargement des données : " + e.getMessage());
        }
        return "withdrawal";
    }

    @PostMapping
    public String makeWithdrawal(@Valid @ModelAttribute("withdrawalDto") WithdrawalDto withdrawalDto, BindingResult bindingResult, Model model) {
        var userActive = userInfos.getUserInfos();

        try {
            transactionService.makeWithdrawal(withdrawalDto);
            model.addAttribute("successMessage", "Retrait effectuée avec succès !");
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", e.getMessage());
        }

        model.addAttribute("transactions", transactionService.getWithdrawalByUserid(userActive.getId()));
        model.addAttribute("userId", withdrawalDto.getUserId());
        model.addAttribute("balance", accountBalanceService.getBalance(withdrawalDto.getUserId()));

        return "withdrawal";
    }
}
