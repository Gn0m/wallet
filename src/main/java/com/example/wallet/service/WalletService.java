package com.example.wallet.service;

import com.example.wallet.dto.ReplenishmentWalletDTO;
import com.example.wallet.dto.WalletDTO;
import com.example.wallet.enums.OperationType;
import com.example.wallet.exception.LowFoundsException;
import com.example.wallet.model.Wallet;
import com.example.wallet.repo.WalletRepo;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.example.wallet.exception.NotFoundWalletException.notFoundWallet;

@Slf4j
@Service
@Validated
public class WalletService implements WalletServiceInterface {

    private final WalletRepo repo;
    private final RLock lock;

    public WalletService(WalletRepo repo, RedissonClient client) {
        this.repo = repo;
        lock = client.getFairLock("lock");
    }

    public WalletDTO getWalletBalance(UUID uuid) {
        val wallet = repo.findById(uuid)
                .orElseThrow(
                        notFoundWallet("Кошелёк {0} не найден", uuid));
        return new WalletDTO(uuid, wallet.getAmount());
    }

    public WalletDTO changeBalance(ReplenishmentWalletDTO dto) {
        BigDecimal amount = dto.amount();
        OperationType operationType = dto.operationType();
        UUID uuid = dto.walletId();
        Wallet wallet;

        wallet = repo.findById(uuid)
                .orElseThrow(
                        notFoundWallet("Кошелёк c uuid {0} не найден", uuid)
                );

        WalletDTO walletDTO;
        BigDecimal afterChangeAmount;
        try {
            lock.lock(2, TimeUnit.SECONDS);
            log.info(Thread.currentThread().getName() + " lock");
            afterChangeAmount = getAfterChangeAmount(operationType, amount, wallet);
        } finally {
            lock.unlock();
            log.info(Thread.currentThread().getName() + " unlock");
        }
        walletDTO = new WalletDTO(uuid, afterChangeAmount);

        return walletDTO;
    }

    private BigDecimal getAfterChangeAmount(OperationType operationType, BigDecimal amount, Wallet wallet) {
        BigDecimal afterChangeAmount = null;

        if (operationType == OperationType.DEPOSIT) {
            Wallet deposit = deposit(amount, wallet);
            afterChangeAmount = repo.save(deposit).getAmount();
        } else if (operationType == OperationType.WITHDRAW) {
            Wallet withdraw = withdraw(amount, wallet);
            afterChangeAmount = repo.save(withdraw).getAmount();
        }
        return afterChangeAmount;
    }

    private Wallet withdraw(BigDecimal amount, Wallet wallet) {

        if (wallet.getAmount().compareTo(amount) < 0) {
            throw new LowFoundsException("Баланс кошелька {0} не достаточен для списания", wallet.getWalletId());
        }

        wallet.setAmount(
                wallet.getAmount()
                        .subtract(amount)
        );
        return wallet;
    }

    private Wallet deposit(BigDecimal amount, Wallet wallet) {
        wallet.setAmount(
                wallet.getAmount()
                        .add(amount)
        );
        return wallet;
    }
}
