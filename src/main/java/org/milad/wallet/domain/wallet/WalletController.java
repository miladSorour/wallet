package org.milad.wallet.domain.wallet;

import lombok.RequiredArgsConstructor;
import org.milad.wallet.domain.transaction.Transaction;
import org.milad.wallet.domain.transaction.TransactionDto;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/wallet")
public class WalletController {

    private final WalletService walletService;

    @PostMapping("/topup")
    public TransactionDto topUp(@AuthenticationPrincipal Jwt jwt, @RequestBody AmountDto a) {
        var tx = walletService.topUp(jwt.getSubject(), a.getAmount());
        return map(tx);
    }

    @PostMapping("/withdraw")
    public TransactionDto withdraw(@AuthenticationPrincipal Jwt jwt, @RequestBody AmountDto a) {
        var tx = walletService.withdraw(jwt.getSubject(), a.getAmount());
        return map(tx);
    }

    @PostMapping("/transfer")
    public String transfer(@AuthenticationPrincipal Jwt jwt, @RequestBody TransferDto t) {
        walletService.transfer(jwt.getSubject(), t.getToEmail(), t.getAmount());
        return "OK";
    }

    private TransactionDto map(Transaction tx) {
        TransactionDto d = new TransactionDto();
        d.setId(tx.getId());
        d.setType(tx.getType());
        d.setAmount(tx.getAmount());
        d.setTimestamp(tx.getTimestamp());
        d.setDetail(tx.getDetail());
        return d;
    }
}