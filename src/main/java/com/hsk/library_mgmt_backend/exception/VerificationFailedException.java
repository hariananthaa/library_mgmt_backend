package com.hsk.library_mgmt_backend.exception;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

@Getter
@Setter
public class VerificationFailedException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 8564977356467509530L;

    public VerificationFailedException() {
        super("Verification failed. Try again.");
    }
}
