package com.hsk.library_mgmt_backend.exception;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

@Getter
@Setter
public class NotFoundException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 2290504629526304151L;

    public NotFoundException() {
        super("Loan not found.");
    }

    public NotFoundException(String message) {
        super(message);
    }
}
