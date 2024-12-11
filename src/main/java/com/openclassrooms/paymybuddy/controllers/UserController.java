package com.openclassrooms.paymybuddy.controllers;

import com.openclassrooms.paymybuddy.dto.UserDto;
import com.openclassrooms.paymybuddy.models.User;
import com.openclassrooms.paymybuddy.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Controller
@RequestMapping("/users")
public class UserController {
    @Autowired
    IUserService userService;

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody UserDto userDto) {
        User user = userService.create(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> modifyUser(@PathVariable Long id, @RequestBody UserDto userDto) {
        User user = userService.modify(id, userDto);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            userService.delete(id);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userService.findAll();
    }

    @PostMapping("/{userId}/relations")
    public String addRelation(@PathVariable Long userId, @RequestParam String friendEmail, Model model) {
        try {
            userService.addRelation(userId, friendEmail);
            model.addAttribute("successMessage", "Relation ajoutée avec succès !");
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", e.getMessage());
        }

        Set<User> relations = userService.getRelations(userId);
        model.addAttribute("relations", relations);

        model.addAttribute("userId", userId);

        return "relation";
    }

    @GetMapping("/{userId}/relations")
    public String getRelations(@PathVariable Long userId, Model model) {
        try {
            Set<User> relations = userService.getRelations(userId);
            model.addAttribute("relations", relations);
            model.addAttribute("userId", userId);
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", "Impossible de récupérer les relations.");
        }

        model.addAttribute("userId", userId);

        return "relation";
    }
}
