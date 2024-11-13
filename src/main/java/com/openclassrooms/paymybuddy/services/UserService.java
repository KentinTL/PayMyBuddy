package com.openclassrooms.paymybuddy.services;

import com.openclassrooms.paymybuddy.dto.UserDto;
import com.openclassrooms.paymybuddy.models.User;
import com.openclassrooms.paymybuddy.repositories.IUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
        return userRepo.save(existingUser);
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
}
