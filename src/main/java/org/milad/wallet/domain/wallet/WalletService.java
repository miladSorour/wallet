package org.milad.wallet.domain.wallet;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.milad.wallet.domain.transaction.Transaction;
import org.milad.wallet.domain.transaction.TransactionRepository;
import org.milad.wallet.domain.transaction.TransactionType;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WalletService {

    private final WalletRepository repository;
    private final TransactionRepository txRepo;

    @Transactional
    public Wallet save(Wallet wallet) {
        return repository.save(wallet);
    }

    public Optional<Wallet> findByUsername(String username) {
        return repository.findByUserUsername(username);
    }

    @Transactional
    public Transaction topUp(String username, double amount) {
        Wallet walletDB = repository.findByUserUsername(username)
                .orElseThrow(() -> new RuntimeException("Wallet not found"));
        walletDB.setBalance(walletDB.getBalance() + amount);
        return txRepo.save(new Transaction(walletDB, TransactionType.TOPUP, amount, "Top-up"));
    }

    @Transactional
    public Transaction withdraw(String username, double amount) {
        Wallet walletDB = repository.findByUserUsername(username)
                .orElseThrow(() -> new RuntimeException("Wallet not found"));
        if (walletDB.getBalance() < amount) throw new RuntimeException("Insufficient funds");
        walletDB.setBalance(walletDB.getBalance() - amount);
        return txRepo.save(new Transaction(walletDB, TransactionType.WITHDRAW, amount, "Withdraw"));
    }

    @Transactional
    public void transfer(String fromUsername, String toUsername, double amount) {
        Wallet src = repository.findByUserUsername(fromUsername)
                .orElseThrow(() -> new RuntimeException("Source wallet not found"));
        Wallet dst = repository.findByUserUsername(toUsername)
                .orElseThrow(() -> new RuntimeException("Destination wallet not found"));
        if (src.getBalance() < amount) throw new RuntimeException("Insufficient funds");
        src.setBalance(src.getBalance() - amount);
        dst.setBalance(dst.getBalance() + amount);
        txRepo.save(new Transaction(src, TransactionType.TRANSFER, amount, "to:" + toUsername));
        txRepo.save(new Transaction(dst, TransactionType.TRANSFER, amount, "from:" + fromUsername));
    }

}