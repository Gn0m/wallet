package com.example.wallet.exception;

import java.text.MessageFormat;
import java.util.function.Supplier;

public class NotFoundWalletException extends RuntimeException {

    public NotFoundWalletException(String message) {
        super(message);
    }

    public NotFoundWalletException(String message, Object... args) {
        super(MessageFormat.format(message, args));
    }

    public static Supplier<NotFoundWalletException> notFoundWallet(String message, Object... args) {
        return () -> new NotFoundWalletException(message, args);
    }

    public static Supplier<NotFoundWalletException> notFoundWallet(String message) {
        return () -> new NotFoundWalletException(message);
    }
}
