package com.openclassrooms.paymybuddy.services;

import com.openclassrooms.paymybuddy.dto.UserDto;
import com.openclassrooms.paymybuddy.models.User;
import com.openclassrooms.paymybuddy.repositories.IUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@AllArgsConstructor
@Service
public class UserService implements IUserService{
    @Autowired
    private IUserRepository userRepo;

    @Override
    public User create(UserDto user) {
        User newUser = new User();
        if (userRepo.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("This email is already used");
        }
        newUser.setUsername(user.getUsername());
        newUser.setEmail(user.getEmail());
        newUser.setPassword(user.getPassword());
        return userRepo.save(newUser);
    }

    @Override
    public User modify(Long id, UserDto user) {
        User existingUser = userRepo.findById(id)
                .orElseThrow(()->new RuntimeException(String.format("This user %s does not exist", id)));
        existingUser.setUsername(user.getUsername());
        existingUser.setEmail(user.getEmail());
        existingUser.setPassword(user.getPassword());
        return userRepo.saveAndFlush(existingUser);
    }

    @Override
    public void delete(Long id) {
        User existingUser = userRepo.findById(id)
                .orElseThrow(()->new RuntimeException(String.format("This user %s does not exist", id)));
        userRepo.delete(existingUser);
    }

    @Override
    public List<User> findAll() {
        return userRepo.findAll();
    }

    public void addRelation(Long userId, String friendEmail) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        User relation = userRepo.findByEmail(friendEmail)
                .orElseThrow(() -> new RuntimeException("Aucun utilisateur trouvé avec cet email"));

        if (Objects.equals(relation.getId(), user.getId())) {
            throw new RuntimeException("Vous ne pouvez pas vous ajouter vous-même !");
        }

        var relationEmails = user.getFriends().stream().map(User::getEmail).toList();

        if (relationEmails.contains(relation.getEmail())) {
            throw new RuntimeException("Cette relation existe déjà.");
        }

        user.getFriends().add(relation);
        userRepo.save(user);
    }

    @Override
    public User getUserById(Long userId) {
        return  userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));
    }

    public Set<User> getRelations(Long userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        return user.getFriends();
    }
}
