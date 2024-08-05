package com.hsk.library_mgmt_backend.exception;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

@Getter
@Setter
public class ParsingException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -7628371761327305846L;

    public ParsingException(String message) {
        super(message);
    }
}

