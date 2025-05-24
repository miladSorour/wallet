package org.milad.wallet.domain.wallet;

import lombok.Data;

@Data
public class DepositRequest {
    private double amount;
    private String username;
}