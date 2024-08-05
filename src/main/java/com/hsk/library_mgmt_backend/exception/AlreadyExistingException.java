package com.hsk.library_mgmt_backend.exception;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

@Setter
@Getter
public class AlreadyExistingException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 4606335354620859217L;

    public AlreadyExistingException(String message) {
        super(message);
    }
}
