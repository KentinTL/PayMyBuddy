package com.openclassrooms.paymybuddy.services;

import com.openclassrooms.paymybuddy.dto.UserDto;
import com.openclassrooms.paymybuddy.models.User;
import com.openclassrooms.paymybuddy.repositories.IUserRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@AllArgsConstructor
@Service
public class UserService implements IUserService {

    @Autowired
    private IUserRepository userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Override
    public User create(UserDto user) {
        logger.info("Création d'un nouvel utilisateur avec l'email: {}", user.getEmail());

        if (userRepo.findByEmail(user.getEmail()).isPresent()) {
            logger.error("Échec de la création : L'email {} est déjà utilisé", user.getEmail());
            throw new RuntimeException("This email is already used");
        }

        User newUser = new User();
        newUser.setUsername(user.getUsername());
        newUser.setEmail(user.getEmail());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));

        User savedUser = userRepo.save(newUser);
        logger.info("Utilisateur créé avec succès : ID={} , Email={}", savedUser.getId(), savedUser.getEmail());

        return savedUser;
    }

    @Override
    public User modify(Long id, UserDto user) {
        logger.info("Modification de l'utilisateur avec l'ID: {}", id);

        User existingUser = userRepo.findById(id)
                .orElseThrow(() -> {
                    logger.error("Échec de la modification : L'utilisateur avec l'ID {} n'existe pas", id);
                    return new RuntimeException(String.format("This user %s does not exist", id));
                });

        existingUser.setUsername(user.getUsername());
        existingUser.setEmail(user.getEmail());
        existingUser.setPassword(passwordEncoder.encode(user.getPassword()));

        User updatedUser = userRepo.saveAndFlush(existingUser);
        logger.info("Utilisateur modifié avec succès : ID={} , Email={}", updatedUser.getId(), updatedUser.getEmail());

        return updatedUser;
    }

    @Override
    public void delete(Long id) {
        logger.info("Suppression de l'utilisateur avec l'ID: {}", id);

        User existingUser = userRepo.findById(id)
                .orElseThrow(() -> {
                    logger.error("Échec de la suppression : L'utilisateur avec l'ID {} n'existe pas", id);
                    return new RuntimeException(String.format("This user %s does not exist", id));
                });

        userRepo.delete(existingUser);
        logger.info("Utilisateur supprimé avec succès : ID={} , Email={}", existingUser.getId(), existingUser.getEmail());
    }

    @Override
    public List<User> findAll() {
        logger.info("Récupération de tous les utilisateurs");
        List<User> users = userRepo.findAll();
        logger.info("Nombre d'utilisateurs récupérés : {}", users.size());
        return users;
    }

    public void addRelation(Long userId, String friendEmail) {
        logger.info("Ajout d'une relation pour l'utilisateur ID: {} avec l'email: {}", userId, friendEmail);

        User user = userRepo.findById(userId)
                .orElseThrow(() -> {
                    logger.error("Échec de l'ajout de relation : Utilisateur introuvable avec l'ID: {}", userId);
                    return new RuntimeException("Utilisateur introuvable");
                });

        User relation = userRepo.findByEmail(friendEmail)
                .orElseThrow(() -> {
                    logger.error("Échec de l'ajout de relation : Aucun utilisateur trouvé avec l'email: {}", friendEmail);
                    return new RuntimeException("Aucun utilisateur trouvé avec cet email");
                });

        if (Objects.equals(relation.getId(), user.getId())) {
            logger.error("Échec de l'ajout de relation : L'utilisateur ne peut pas s'ajouter lui-même. ID: {}", userId);
            throw new RuntimeException("Vous ne pouvez pas vous ajouter vous-même !");
        }

        var relationEmails = user.getFriends().stream().map(User::getEmail).toList();

        if (relationEmails.contains(relation.getEmail())) {
            logger.error("Échec de l'ajout de relation : La relation avec l'email {} existe déjà", friendEmail);
            throw new RuntimeException("Cette relation existe déjà.");
        }

        user.getFriends().add(relation);
        userRepo.save(user);
        logger.info("Relation ajoutée avec succès entre l'utilisateur ID {} et l'email {}", userId, friendEmail);
    }

    @Override
    public User getUserById(Long userId) {
        logger.info("Récupération de l'utilisateur avec l'ID: {}", userId);

        return userRepo.findById(userId)
                .orElseThrow(() -> {
                    logger.error("Échec de la récupération : Utilisateur introuvable avec l'ID: {}", userId);
                    return new RuntimeException("Utilisateur introuvable");
                });
    }

    public Set<User> getRelations(Long userId) {
        logger.info("Récupération des relations pour l'utilisateur avec l'ID: {}", userId);

        User user = userRepo.findById(userId)
                .orElseThrow(() -> {
                    logger.error("Échec de la récupération des relations : Utilisateur introuvable avec l'ID: {}", userId);
                    return new RuntimeException("Utilisateur introuvable");
                });

        Set<User> relations = user.getFriends();
        logger.info("Nombre de relations récupérées pour l'utilisateur ID {}: {}", userId, relations.size());

        return relations;
    }
}
