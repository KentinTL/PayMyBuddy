package com.openclassrooms.paymybuddy.services;

import com.openclassrooms.paymybuddy.models.User;

public interface IAuthService {
    public User auth(String email, String password);
}
