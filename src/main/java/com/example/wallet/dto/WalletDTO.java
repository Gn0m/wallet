package com.example.wallet.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record WalletDTO(UUID walletId, BigDecimal amount) {
}
