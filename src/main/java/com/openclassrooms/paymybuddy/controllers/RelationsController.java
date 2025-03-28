package com.openclassrooms.paymybuddy.controllers;

import com.openclassrooms.paymybuddy.dto.RelationDto;
import com.openclassrooms.paymybuddy.models.User;
import com.openclassrooms.paymybuddy.services.IUserService;
import com.openclassrooms.paymybuddy.services.UserInfos;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Set;

@Controller
@RequestMapping("/relation")
public class RelationsController {
    @Autowired
    IUserService userService;

    @Autowired
    private UserInfos userInfos;

    @PostMapping
    public String addRelation(@Valid @ModelAttribute("relationDto") RelationDto relationDto, Model model) {
        var userId = userInfos.getUserInfos().getId();
        try {
            userService.addRelation(userId, relationDto.getFriendEmail());
            model.addAttribute("successMessage", "Relation ajoutée avec succès !");
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", e.getMessage());
        }

        Set<User> relations = userService.getRelations(userId);
        model.addAttribute("relations", relations);

        model.addAttribute("userId", userId);

        return "relation";
    }

    @GetMapping
    public String relationPage(Model model) {
        var userId = userInfos.getUserInfos().getId();
        try {
            model.addAttribute("relations", userService.getRelations(userId));
            model.addAttribute("userId", userId);
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", "Impossible de récupérer les relations.");
        }

        model.addAttribute("userId", userId);

        return "relation";
    }
}
