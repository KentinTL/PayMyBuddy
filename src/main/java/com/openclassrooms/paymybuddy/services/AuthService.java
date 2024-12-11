package com.openclassrooms.paymybuddy.services;

import com.openclassrooms.paymybuddy.models.User;
import com.openclassrooms.paymybuddy.repositories.IUserRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService implements IAuthService{
    @Autowired
    private IUserRepository userRepository;

    public User auth(String email, String password) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }
}
