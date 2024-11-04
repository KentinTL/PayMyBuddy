package com.openclassrooms.paymybuddy.repository;

import com.openclassrooms.paymybuddy.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InterfaceUsersRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByUsername(String username);
}
