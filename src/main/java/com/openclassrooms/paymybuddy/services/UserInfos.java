package com.openclassrooms.paymybuddy.services;

import com.openclassrooms.paymybuddy.models.User;
import com.openclassrooms.paymybuddy.repositories.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserInfos {
    @Autowired
    private IUserRepository userRepository;

    public User getUserInfos(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        var email = auth.getName();

        return userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not Found with email : " + email));
    }
}
