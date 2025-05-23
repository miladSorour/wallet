package org.milad.wallet.domain.wallet;

import lombok.Data;

@Data
public class TransferDto { private String toUsername; private double amount; }
