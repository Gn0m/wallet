package com.example.wallet.controller;

import com.example.wallet.dto.ReplenishmentWalletDTO;
import com.example.wallet.enums.OperationType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = Configuration.class)
@AutoConfigureMockMvc
class WalletControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    private ReplenishmentWalletDTO dto;

    @BeforeEach
    void setUp() {
        dto = new ReplenishmentWalletDTO(
                UUID.fromString("81f1344e-3d15-49c2-95b7-5e26a772c10a"),
                OperationType.DEPOSIT,
                new BigDecimal(200)
        );
    }

    @Test
    void getAmount() throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/api/v1/wallets/42cce541-80d8-43a6-85b0-b50f45659ba4"))
                .andExpectAll(
                        status().isOk(),
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
                        content().json("""
                                   {
                                       "walletId": "42cce541-80d8-43a6-85b0-b50f45659ba4",
                                       "amount": 500
                                   }
                                """)

                );
    }

    @Test
    void addAmount() throws Exception {
        String json = objectMapper.writeValueAsString(dto);
        this.mockMvc
                .perform(MockMvcRequestBuilders.post("http://localhost:8080/api/v1/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON)
                ).andExpectAll(
                        status().isOk(),
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
                        content().json("""
                                   {
                                       "walletId": "81f1344e-3d15-49c2-95b7-5e26a772c10a",
                                       "amount": 400
                                   }
                                """)
                );
    }
}