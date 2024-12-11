package com.openclassrooms.paymybuddy.services;

import com.openclassrooms.paymybuddy.dto.UserDto;
import com.openclassrooms.paymybuddy.models.User;

import java.util.List;
import java.util.Set;

public interface IUserService {
    User create(UserDto user);
    User modify(Long id, UserDto user);
    void delete(Long id);
    List<User> findAll();
    Set<User> getRelations(Long userId);
    void addRelation(Long userId, String friendEmail);
}
