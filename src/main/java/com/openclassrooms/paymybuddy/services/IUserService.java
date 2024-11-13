package com.openclassrooms.paymybuddy.services;

import com.openclassrooms.paymybuddy.dto.UserDto;
import com.openclassrooms.paymybuddy.models.User;

import java.util.List;

public interface IUserService {
    User create(UserDto user);
    User modify(Long id, UserDto user);
    void delete(Long id);
    List<User> findAll();
}
