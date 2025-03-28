package com.openclassrooms.paymybuddy.repositories;

import com.openclassrooms.paymybuddy.models.Transaction;
import com.openclassrooms.paymybuddy.models.TransactionType;
import com.openclassrooms.paymybuddy.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ITransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findBySenderAndTransactionType(User sender, TransactionType transactionType);
}
