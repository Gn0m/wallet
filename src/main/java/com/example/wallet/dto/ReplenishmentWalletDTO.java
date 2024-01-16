package com.example.wallet.dto;

import com.example.wallet.enums.OperationType;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;


public record ReplenishmentWalletDTO(@NotNull(message = "walletId not null") UUID walletId,
                                     @NotNull(message = "operationType not null") OperationType operationType,
                                     @NotNull(message = "amount not null") BigDecimal amount) {
}
