package com.hsk.library_mgmt_backend.exception;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

@Setter
@Getter
public class PaymentException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -5043872545836107157L;

    public PaymentException(String message) {
        super(message);
    }
}
