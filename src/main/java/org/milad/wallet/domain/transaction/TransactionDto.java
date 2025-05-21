package org.milad.wallet.domain.transaction;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TransactionDto {
    private Long id;
    private TransactionType type;
    private double amount;
    private LocalDateTime timestamp;
    private String detail;
}