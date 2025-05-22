package org.milad.wallet.domain.transaction;

import lombok.RequiredArgsConstructor;
import org.milad.wallet.domain.wallet.Wallet;
import org.milad.wallet.domain.wallet.WalletService;
import org.milad.wallet.exception.RecordNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final WalletService walletService;
    private final TransactionRepository repository;

    public Page<Transaction> getHistory(String username, Pageable pageable) {
        Wallet walletDB = walletService.findByUsername(username).orElseThrow(() -> new RecordNotFoundException(Wallet.class.getSimpleName()));
        return repository.findByWalletOrderByTimestampDesc(walletDB, pageable);
    }
}
