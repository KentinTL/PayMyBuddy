package com.openclassrooms.paymybuddy.services;

import com.openclassrooms.paymybuddy.models.Transaction;
import com.openclassrooms.paymybuddy.models.TransactionType;
import com.openclassrooms.paymybuddy.repositories.ITransactionRepository;
import com.openclassrooms.paymybuddy.repositories.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

public class AccountBalanceService {
    @Autowired
    private ITransactionRepository iTransactionRepository;

    @Autowired
    private IUserRepository userRepository;

    public BigDecimal getBalance(Long userId) {
        userRepository.findById(userId).orElseThrow(() -> new RuntimeException("No user found with this id : " + userId));
        var deposits = iTransactionRepository.findAll().stream()
                .filter(transaction -> (transaction.getTransactionType().equals(TransactionType.DEPOSIT) ||
                        (transaction.getTransactionType().equals(TransactionType.TRANSFER) && transaction.getReceiver().getId().equals(userId))))
                .map(Transaction::getAmount)
                .mapToDouble(Double::doubleValue)
                .sum();
        var withdrawals = iTransactionRepository.findAll().stream()
                .filter(transaction -> (transaction.getTransactionType().equals(TransactionType.WITHDRAWAL) ||
                        (transaction.getTransactionType().equals(TransactionType.TRANSFER) && transaction.getSender().getId().equals(userId))))
                .map(Transaction::getAmount)
                .mapToDouble(Double::doubleValue)
                .sum();
        return BigDecimal.valueOf(deposits).subtract(BigDecimal.valueOf(withdrawals));
    }
}