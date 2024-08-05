package com.hsk.library_mgmt_backend.exception;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

@Getter
@Setter
public class ValidationException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -6721310947552280007L;

    public ValidationException(String message) {
        super(message);
    }

}
