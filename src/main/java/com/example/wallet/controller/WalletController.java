package com.example.wallet.controller;

import com.example.wallet.dto.ReplenishmentWalletDTO;
import com.example.wallet.dto.WalletDTO;
import com.example.wallet.service.WalletServiceInterface;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class WalletController {

    private final WalletServiceInterface service;

    public WalletController(WalletServiceInterface service) {
        this.service = service;
    }


    @GetMapping("/wallets/{uuid}")
    public ResponseEntity<WalletDTO> getAmount(@PathVariable("uuid") UUID uuid) {
        return new ResponseEntity<>(service.getWalletBalance(uuid), HttpStatus.OK);
    }

    @PostMapping("/wallet")
    public ResponseEntity<WalletDTO> addAmount(@Valid @RequestBody ReplenishmentWalletDTO dto) {
        return new ResponseEntity<>(service.changeBalance(dto), HttpStatus.OK);
    }
}
