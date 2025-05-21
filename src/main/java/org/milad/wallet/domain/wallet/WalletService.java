package org.milad.wallet.domain.wallet;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.milad.wallet.domain.transaction.Transaction;
import org.milad.wallet.domain.transaction.TransactionRepository;
import org.milad.wallet.domain.transaction.TransactionType;
import org.milad.wallet.domain.user.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WalletService {

    private final UserRepository userRepo;
    private final WalletRepository walletRepo;
    private final TransactionRepository txRepo;

    @Transactional
    public Transaction topUp(String email, double amount) {
        Wallet w = walletRepo.findByUserEmailForUpdate(email)
                .orElseThrow(() -> new RuntimeException("Wallet not found"));
        w.setBalance(w.getBalance() + amount);
        return txRepo.save(new Transaction(w, TransactionType.TOPUP, amount, "Top-up"));
    }

    @Transactional
    public Transaction withdraw(String email, double amount) {
        Wallet w = walletRepo.findByUserEmailForUpdate(email)
                .orElseThrow(() -> new RuntimeException("Wallet not found"));
        if (w.getBalance() < amount) throw new RuntimeException("Insufficient funds");
        w.setBalance(w.getBalance() - amount);
        return txRepo.save(new Transaction(w, TransactionType.WITHDRAW, amount, "Withdraw"));
    }

    @Transactional
    public void transfer(String fromEmail, String toEmail, double amount) {
        Wallet src = walletRepo.findByUserEmailForUpdate(fromEmail)
                .orElseThrow(() -> new RuntimeException("Source wallet not found"));
        Wallet dst = walletRepo.findByUserEmailForUpdate(toEmail)
                .orElseThrow(() -> new RuntimeException("Destination wallet not found"));
        if (src.getBalance() < amount) throw new RuntimeException("Insufficient funds");
        src.setBalance(src.getBalance() - amount);
        dst.setBalance(dst.getBalance() + amount);
        txRepo.save(new Transaction(src, TransactionType.TRANSFER, amount, "to:" + toEmail));
        txRepo.save(new Transaction(dst, TransactionType.TRANSFER, amount, "from:" + fromEmail));
    }

    public List<Transaction> getHistory(String email) {
        Wallet w = walletRepo.findByUserEmailForUpdate(email)
                .orElseThrow(() -> new RuntimeException("Wallet not found"));
        return txRepo.findByWalletOrderByTimestampDesc(w);
    }
}