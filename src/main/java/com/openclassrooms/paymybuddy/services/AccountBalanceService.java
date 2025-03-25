package com.openclassrooms.paymybuddy.services;

import com.openclassrooms.paymybuddy.models.Transaction;
import com.openclassrooms.paymybuddy.models.TransactionType;
import com.openclassrooms.paymybuddy.repositories.ITransactionRepository;
import com.openclassrooms.paymybuddy.repositories.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

@Service
public class AccountBalanceService implements IAccountBalanceService {

    private static final Logger logger = LoggerFactory.getLogger(AccountBalanceService.class);

    @Autowired
    private ITransactionRepository iTransactionRepository;

    @Autowired
    private IUserRepository userRepository;

    public BigDecimal getBalance(Long userId) {
        logger.info("Calcul du solde pour l'utilisateur ID : {}", userId);

        userRepository.findById(userId)
                .orElseThrow(() -> {
                    logger.error("Aucun utilisateur trouvé avec l'ID : {}", userId);
                    return new RuntimeException("No user found with this id : " + userId);
                });

        var deposits = iTransactionRepository.findAll().stream()
                .filter(transaction -> (
                        (transaction.getTransactionType().equals(TransactionType.DEPOSIT) && transaction.getSender().getId().equals(userId)) ||
                                (transaction.getTransactionType().equals(TransactionType.TRANSFER) && transaction.getReceiver().getId().equals(userId))))
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        var withdrawals = iTransactionRepository.findAll().stream()
                .filter(transaction -> (
                        (transaction.getTransactionType().equals(TransactionType.WITHDRAWAL) && transaction.getSender().getId().equals(userId)) ||
                                (transaction.getTransactionType().equals(TransactionType.TRANSFER) && transaction.getSender().getId().equals(userId))))
                .map(Transaction::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal balance = deposits.subtract(withdrawals);

        logger.info("Solde calculé pour l'utilisateur ID {} : {}€ (Dépôts : {}€, Retraits : {}€)",
                userId, balance, deposits, withdrawals);

        return balance;
    }
}
