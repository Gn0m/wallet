package com.example.wallet.exception;

import java.text.MessageFormat;

public class LowFoundsException extends RuntimeException {

    public LowFoundsException(String message, Object... args) {
        super(MessageFormat.format(message, args));
    }


}
