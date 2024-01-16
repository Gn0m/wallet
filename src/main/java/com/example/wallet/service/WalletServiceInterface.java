package com.example.wallet.service;

import com.example.wallet.dto.ReplenishmentWalletDTO;
import com.example.wallet.dto.WalletDTO;

import java.util.UUID;

public interface WalletServiceInterface {

    WalletDTO getWalletBalance(UUID uuid);

    WalletDTO changeBalance(ReplenishmentWalletDTO dto);
}
