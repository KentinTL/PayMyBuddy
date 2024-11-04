package com.openclassrooms.paymybuddy.service;

import com.openclassrooms.paymybuddy.models.Users;
import com.openclassrooms.paymybuddy.repository.InterfaceUsersRepository;
import org.springframework.stereotype.Service;

@Service
public class UsersService {
    private InterfaceUsersRepository Iusersrepo;

    public void addUser(Users user) {
        Iusersrepo.save(user);
    }

    public void searchUser(String username) {
        Users user = Iusersrepo.findByUsername(username).orElseThrow(()->new RuntimeException("Y'a personne"));
    }
}
