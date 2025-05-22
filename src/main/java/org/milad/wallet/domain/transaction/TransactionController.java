package org.milad.wallet.domain.transaction;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.milad.wallet.common.Pagination;
import org.milad.wallet.domain.wallet.AmountDto;
import org.milad.wallet.domain.wallet.TransferDto;
import org.milad.wallet.domain.wallet.WalletService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/transaction")
public class TransactionController {

    private final TransactionService service;

    @GetMapping("/history")
    public Page<TransactionDto> history(@AuthenticationPrincipal Jwt jwt, @Valid @ModelAttribute Pagination pagination) {
        Sort sort = Sort.by(Sort.Direction.fromString(pagination.getDirection()), pagination.getSortBy());
        PageRequest pageRequest = PageRequest.of(pagination.getPage(), pagination.getSize(), sort);
        Page<Transaction> transactions = service.getHistory(jwt.getSubject(), pageRequest);
        return transactions.map(this::map);
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