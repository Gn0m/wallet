package com.example.wallet.controller;

import com.example.wallet.WalletApplication;
import org.springframework.boot.SpringApplication;

public class TestWalletApplication {

    public static void main(String[] args) {
        SpringApplication.from(WalletApplication::main)
                .with(Configuration.class)
                .run(args);
    }
}
