package org.milad.wallet.domain.transaction;

import org.milad.wallet.domain.wallet.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByWalletOrderByTimestampDesc(Wallet wallet);
}