package org.milad.wallet.domain.wallet;

import lombok.Data;

@Data
public class TransferDto { private String toEmail; private double amount; }
