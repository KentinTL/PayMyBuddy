package com.openclassrooms.paymybuddy.repositories;

import com.openclassrooms.paymybuddy.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ITransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findBySenderIdOrReceiverId(Long senderId, Long receiverId);
}
