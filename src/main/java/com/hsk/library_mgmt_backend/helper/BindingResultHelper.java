package com.hsk.library_mgmt_backend.helper;

import com.hsk.library_mgmt_backend.exception.ValidationException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

public class BindingResultHelper {
    private BindingResultHelper() {
    }

    public static void processBindingResult(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            FieldError fieldError = bindingResult.getFieldError();
            assert fieldError != null;
            String errorMessage = fieldError.getDefaultMessage();
            throw new ValidationException(errorMessage);
        }
    }

}
