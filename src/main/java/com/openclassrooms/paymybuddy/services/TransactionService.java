package com.openclassrooms.paymybuddy.services;

import com.openclassrooms.paymybuddy.dto.DepositDto;
import com.openclassrooms.paymybuddy.dto.TransferDto;
import com.openclassrooms.paymybuddy.dto.WithdrawalDto;
import com.openclassrooms.paymybuddy.models.Transaction;
import com.openclassrooms.paymybuddy.models.TransactionType;
import com.openclassrooms.paymybuddy.models.User;
import com.openclassrooms.paymybuddy.repositories.ITransactionRepository;
import com.openclassrooms.paymybuddy.repositories.IUserRepository;
import com.openclassrooms.paymybuddy.tools.PercentageCalculator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
public class TransactionService {

    @Autowired
    private ITransactionRepository transactionRepo;

    @Autowired
    private IUserRepository userRepo;

    @Autowired
    private IAccountBalanceService accountBalanceService;

    @Autowired
    private PercentageCalculator percentageCalculator;

    private static final Logger logger = LoggerFactory.getLogger(TransactionService.class);

    @Transactional(rollbackFor = Exception.class)
    public Transaction makeTransfer(TransferDto transferDto) {
        logger.info("Démarrage du transfert pour l'utilisateur ID : {}", transferDto.getUserId());

        try {
            User sender = userRepo.findById(transferDto.getUserId())
                    .orElseThrow(() -> {
                        logger.error("Échec du transfert : Expéditeur introuvable (ID : {})", transferDto.getUserId());
                        return new RuntimeException("Expéditeur introuvable.");
                    });

            User receiver = userRepo.findById(transferDto.getReceiverId())
                    .orElseThrow(() -> {
                        logger.error("Échec du transfert : Destinataire introuvable (ID : {})", transferDto.getReceiverId());
                        return new RuntimeException("Destinataire introuvable.");
                    });

            BigDecimal userBalance = accountBalanceService.getBalance(transferDto.getUserId());

            if (transferDto.getAmount().compareTo(userBalance) > 0) {
                logger.error("Échec du transfert : Solde insuffisant pour l'utilisateur ID : {}", sender.getId());
                throw new RuntimeException("Solde insuffisant");
            }

            if (!sender.getFriends().contains(receiver)) {
                logger.error("Échec du transfert : Le destinataire (ID : {}) n'est pas une relation de l'expéditeur (ID : {})",
                        receiver.getId(), sender.getId());
                throw new RuntimeException("Le destinataire doit être une relation de l'expéditeur.");
            }

            BigDecimal fee = percentageCalculator.calculPercent(transferDto.getAmount());
            BigDecimal totalAmount = transferDto.getAmount().add(fee);

            if (totalAmount.compareTo(userBalance) > 0) {
                logger.error("Échec du transfert : Solde insuffisant pour couvrir les frais de transaction. ID utilisateur : {}",
                        sender.getId());
                throw new RuntimeException("Solde insuffisant pour couvrir les frais de transaction.");
            }

            Transaction transaction = new Transaction();
            transaction.setSender(sender);
            transaction.setReceiver(receiver);
            transaction.setAmount(transferDto.getAmount());
            transaction.setDate(LocalDateTime.now());
            transaction.setDescription(transferDto.getDescription());
            transaction.setTransactionType(TransactionType.TRANSFER);
            transaction.setFee(fee);

            logger.info("Transfert réussi : {}€ de l'utilisateur {} vers {} (Frais : {}€)",
                    transferDto.getAmount(), sender.getId(), receiver.getId(), fee);

            return transactionRepo.save(transaction);

        } catch (Exception e) {
            logger.error("Erreur lors du transfert pour l'utilisateur ID : {} - {}", transferDto.getUserId(), e.getMessage(), e);
            throw e;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public Transaction makeDeposit(DepositDto depositDto) {
        logger.info("Dépôt en cours pour l'utilisateur ID: {}", depositDto.getUserId());

        User sender = userRepo.findById(depositDto.getUserId())
                .orElseThrow(() -> {
                    logger.error("Échec du dépôt : Expéditeur introuvable (ID: {})", depositDto.getUserId());
                    return new RuntimeException("Expéditeur introuvable.");
                });

        Transaction transaction = new Transaction();
        transaction.setSender(sender);
        transaction.setReceiver(sender);
        transaction.setAmount(depositDto.getAmount());
        transaction.setDate(LocalDateTime.now());
        transaction.setDescription(depositDto.getDescription());
        transaction.setTransactionType(TransactionType.DEPOSIT);
        transaction.setFee(BigDecimal.ZERO);

        logger.debug("Détails du dépôt : Montant={}€, Description={}",
                depositDto.getAmount(), depositDto.getDescription());

        Transaction savedTransaction = transactionRepo.save(transaction);

        logger.info("Dépôt réussi : {}€ ajouté au compte de l'utilisateur ID {}",
                savedTransaction.getAmount(), sender.getId());

        return savedTransaction;
    }

    @Transactional(rollbackFor = Exception.class)
    public Transaction makeWithdrawal(WithdrawalDto withdrawalDto) {
        logger.info("Demande de retrait en cours pour l'utilisateur ID: {}", withdrawalDto.getUserId());

        User sender = userRepo.findById(withdrawalDto.getUserId())
                .orElseThrow(() -> {
                    logger.error("Échec du retrait : Expéditeur introuvable (ID: {})", withdrawalDto.getUserId());
                    return new RuntimeException("Expéditeur introuvable.");
                });

        BigDecimal currentBalance = accountBalanceService.getBalance(withdrawalDto.getUserId());

        if (withdrawalDto.getAmount().compareTo(currentBalance) > 0) {
            logger.error("Échec du retrait : Solde insuffisant pour l'utilisateur ID {} (Solde actuel: {}€, Montant demandé: {}€)",
                    withdrawalDto.getUserId(), currentBalance, withdrawalDto.getAmount());
            throw new RuntimeException("Solde insuffisant");
        }

        Transaction transaction = new Transaction();
        transaction.setSender(sender);
        transaction.setReceiver(sender);
        transaction.setAmount(withdrawalDto.getAmount());
        transaction.setDate(LocalDateTime.now());
        transaction.setDescription(withdrawalDto.getDescription());
        transaction.setTransactionType(TransactionType.WITHDRAWAL);
        transaction.setFee(BigDecimal.ZERO);

        logger.debug("Détails du retrait : Montant={}€, Description={}",
                withdrawalDto.getAmount(), withdrawalDto.getDescription());

        Transaction savedTransaction = transactionRepo.save(transaction);

        logger.info("Retrait réussi : {}€ retiré du compte de l'utilisateur ID {}",
                savedTransaction.getAmount(), sender.getId());

        return savedTransaction;
    }

    public List<Transaction> getTransactionsByUserId(Long userId) {
        logger.info("Récupération des transactions pour l'utilisateur ID: {}", userId);

        User sender = userRepo.findById(userId)
                .orElseThrow(() -> {
                    logger.error("Expéditeur introuvable avec l'ID: {}", userId);
                    return new RuntimeException("Expéditeur introuvable.");
                });

        var transaction = transactionRepo.findBySenderAndTransactionType(sender, TransactionType.TRANSFER);
        transaction.sort(Comparator.comparing(Transaction::getDate).reversed());

        logger.info("Transactions récupérées pour l'utilisateur ID: {}. Nombre de transactions : {}", userId, transaction.size());
        return transaction;
    }

    public List<Transaction> getDepositByUserid(Long userId) {
        logger.info("Récupération des dépôts pour l'utilisateur ID: {}", userId);

        User sender = userRepo.findById(userId)
                .orElseThrow(() -> {
                    logger.error("Expéditeur introuvable avec l'ID: {}", userId);
                    return new RuntimeException("Expéditeur introuvable.");
                });

        var deposit = transactionRepo.findBySenderAndTransactionType(sender, TransactionType.DEPOSIT);
        deposit.sort(Comparator.comparing(Transaction::getDate).reversed());

        logger.info("Dépôts récupérés pour l'utilisateur ID: {}. Nombre de dépôts : {}", userId, deposit.size());
        return deposit;
    }

    public List<Transaction> getWithdrawalByUserid(Long userId) {
        logger.info("Récupération des retraits pour l'utilisateur ID: {}", userId);

        User sender = userRepo.findById(userId)
                .orElseThrow(() -> {
                    logger.error("Expéditeur introuvable avec l'ID: {}", userId);
                    return new RuntimeException("Expéditeur introuvable.");
                });

        var deposit = transactionRepo.findBySenderAndTransactionType(sender, TransactionType.WITHDRAWAL);
        deposit.sort(Comparator.comparing(Transaction::getDate).reversed());

        logger.info("Retraits récupérés pour l'utilisateur ID: {}. Nombre de retraits : {}", userId, deposit.size());
        return deposit;
    }
}
