package org.milad.wallet.domain.wallet;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.milad.wallet.domain.transaction.Transaction;
import org.milad.wallet.domain.transaction.TransactionRepository;
import org.milad.wallet.domain.transaction.TransactionType;
import org.milad.wallet.exception.InsufficientBalanceException;
import org.milad.wallet.exception.RecordNotFoundException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WalletService {

    private final WalletRepository repository;
    private final TransactionRepository transactionRepository;

    @Transactional
    public Wallet save(Wallet wallet) {
        return repository.save(wallet);
    }

    public Optional<Wallet> findByUsername(String username) {
        return repository.findByUserUsername(username);
    }

    @Transactional
    public Transaction topUp(String username, double amount) {
        Wallet walletDB = repository.findByUserUsername(username).orElseThrow(() -> new RuntimeException("Wallet not found"));
        walletDB.setBalance(walletDB.getBalance() + amount);
        return transactionRepository.save(new Transaction(walletDB, TransactionType.TOPUP, amount, "Top-up"));
    }

    @Transactional
    public Transaction withdraw(String username, double amount) {
        Wallet walletDB = repository.findByUserUsername(username)
                .orElseThrow(() -> new RuntimeException("Wallet not found"));
        if (walletDB.getBalance() < amount) throw new RuntimeException("Insufficient funds");
        walletDB.setBalance(walletDB.getBalance() - amount);
        return transactionRepository.save(new Transaction(walletDB, TransactionType.WITHDRAW, amount, "Withdraw"));
    }

    @Transactional
    @Retryable(
            value = {ObjectOptimisticLockingFailureException.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 100, multiplier = 2)
    )
    public void transfer(String fromUsername, String toUsername, double amount) {
        Wallet src = repository.findByUserUsername(fromUsername).orElseThrow(() -> new RecordNotFoundException("Source wallet"));
        Wallet dst = repository.findByUserUsername(toUsername).orElseThrow(() -> new RecordNotFoundException("Destination wallet"));

        if (src.getBalance() < amount) throw new InsufficientBalanceException();

        src.setBalance(src.getBalance() - amount);
        dst.setBalance(dst.getBalance() + amount);

        transactionRepository.save(new Transaction(src, TransactionType.TRANSFER, amount, "to:" + toUsername));
        transactionRepository.save(new Transaction(dst, TransactionType.TRANSFER, amount, "from:" + fromUsername));
    }

    public Wallet findByUser(Long userId) {
        return repository.findByUser(userId).orElseThrow(() -> new RecordNotFoundException("Wallet"));
    }
}