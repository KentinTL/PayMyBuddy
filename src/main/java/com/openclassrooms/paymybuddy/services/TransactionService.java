package com.openclassrooms.paymybuddy.services;

import com.openclassrooms.paymybuddy.dto.TransferDto;
import com.openclassrooms.paymybuddy.models.Transaction;
import com.openclassrooms.paymybuddy.models.TransactionType;
import com.openclassrooms.paymybuddy.models.User;
import com.openclassrooms.paymybuddy.repositories.ITransactionRepository;
import com.openclassrooms.paymybuddy.repositories.IUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionService {

    @Autowired
    private ITransactionRepository transactionRepo;

    @Autowired
    private IUserRepository userRepo;

    @Autowired
    private AccountBalanceService accountBalanceService;

    public Transaction makeTransfer(TransferDto transferDto) {

        // Récupérer l'expéditeur et le destinataire
        User sender = userRepo.findById(transferDto.getUserId())
                .orElseThrow(() -> new RuntimeException("Expéditeur introuvable."));
        User receiver = userRepo.findById(transferDto.getReceiverId())
                .orElseThrow(() -> new RuntimeException("Destinataire introuvable."));

        BigDecimal userBalance = accountBalanceService.getBalance(transferDto.getUserId());

        if (transferDto.getAmount().compareTo(userBalance) > 0) {
            throw new RuntimeException("Solde insufisant");
        }

        // Vérification si le destinataire est un ami
        if (!sender.getFriends().contains(receiver)) {
            throw new RuntimeException("Le destinataire doit être une relation de l'expéditeur.");
        }

        // Création de la transaction
        Transaction transaction = new Transaction();
        transaction.setSender(sender);
        transaction.setReceiver(receiver);
        transaction.setAmount(transferDto.getAmount());
        transaction.setDate(LocalDateTime.now());
        transaction.setDescription(transferDto.getDescription());
        transaction.setTransactionType(TransactionType.TRANSFER);

        return transactionRepo.save(transaction);
    }

    public Transaction makeDeposit(Long userId, BigDecimal amount, String description) {
        
    }

    public List<Transaction> getTransactionsByUserId(Long userId) {
        return transactionRepo.findBySenderIdOrReceiverId(userId, userId);
    }
}
