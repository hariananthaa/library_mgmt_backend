package com.hsk.library_mgmt_backend.exception.advice;


import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.hsk.library_mgmt_backend.exception.AlreadyExistingException;
import com.hsk.library_mgmt_backend.exception.NotFoundException;
import com.hsk.library_mgmt_backend.exception.VerificationFailedException;
import com.hsk.library_mgmt_backend.response.ResponseData;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import jakarta.validation.constraints.NotNull;
import liquibase.exception.DatabaseException;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("LoggingPlaceholderCountMatchesArgumentCount") // Disabling this warning for debugging logs clearly
@Slf4j
@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {
    private static final String ERROR_MSG_LITERAL = "exception {}";
    @Value("${spring.application.name}")
    private String serviceName;

    @ExceptionHandler(value = {NotFoundException.class})
    protected ResponseEntity<ResponseData<Object>> handleException(NotFoundException e) {
        ResponseData<Object> responseData = buildExceptionResponseData(e.getMessage(), HttpStatus.OK.value());
        log.error(ERROR_MSG_LITERAL, e);
        return ResponseEntity.ok().body(responseData);
    }

    @ExceptionHandler(value = {BadRequestException.class})
    protected ResponseEntity<ResponseData<Object>> handleException(BadRequestException e) {
        ResponseData<Object> responseData = buildExceptionResponseData(e.getMessage(), HttpStatus.OK.value());
        log.error(ERROR_MSG_LITERAL, e);
        return ResponseEntity.ok().body(responseData);
    }

    @ExceptionHandler(value = {DateTimeParseException.class})
    protected ResponseEntity<ResponseData<Object>> handleException(DateTimeParseException e) {
        ResponseData<Object> responseData = buildExceptionResponseData(e.getMessage(), HttpStatus.OK.value());
        log.error(ERROR_MSG_LITERAL, e);
        return ResponseEntity.ok().body(responseData);
    }

    @ExceptionHandler(value = {InvalidFormatException.class})
    protected ResponseEntity<ResponseData<Object>> handleException(InvalidFormatException e) {
        ResponseData<Object> responseData = buildExceptionResponseData(e.getMessage(), HttpStatus.OK.value());
        log.error(ERROR_MSG_LITERAL, e);
        return ResponseEntity.ok().body(responseData);
    }

    protected ResponseData<Object> buildExceptionResponseData(String message, Integer status) {
        ResponseData<Object> responseData = new ResponseData<>();
        responseData.setMessage(message);
        responseData.setData(null);
        responseData.setStatus(status);
        responseData.setResult(false);
        return responseData;
    }

    @ExceptionHandler(value = {ValidationException.class})
    protected ResponseEntity<ResponseData<Object>> handleException(ValidationException e) {
        ResponseData<Object> responseData = buildExceptionResponseData(e.getMessage(), HttpStatus.OK.value());
        log.error(ERROR_MSG_LITERAL, e);
        return ResponseEntity.ok().body(responseData);
    }

    @ExceptionHandler(value = {BadCredentialsException.class})
    protected ResponseEntity<ResponseData<Object>> handleException(BadCredentialsException e) {
        ResponseData<Object> responseData = buildExceptionResponseData(e.getMessage(), HttpStatus.OK.value());
        log.error(ERROR_MSG_LITERAL, e);
        return ResponseEntity.ok().body(responseData);
    }

    @ExceptionHandler(value = {NumberFormatException.class})
    protected ResponseEntity<ResponseData<Object>> handleException(NumberFormatException e) {
        ResponseData<Object> responseData = buildExceptionResponseData(e.getMessage(), HttpStatus.OK.value());
        log.error(ERROR_MSG_LITERAL, e);
        return ResponseEntity.ok().body(responseData);
    }

    @ExceptionHandler(value = {AlreadyExistingException.class})
    protected ResponseEntity<ResponseData<Object>> handleException(AlreadyExistingException e) {
        ResponseData<Object> responseData = buildExceptionResponseData(e.getMessage(), HttpStatus.OK.value());
        log.error(ERROR_MSG_LITERAL, e);
        return ResponseEntity.ok().body(responseData);
    }

    @ExceptionHandler(value = {VerificationFailedException.class})
    protected ResponseEntity<ResponseData<Object>> handleException(VerificationFailedException e) {
        ResponseData<Object> responseData = buildExceptionResponseData(e.getMessage(), HttpStatus.OK.value());
        log.error(ERROR_MSG_LITERAL, e);
        return ResponseEntity.ok().body(responseData);
    }

    @ExceptionHandler(value = {ConstraintViolationException.class})
    protected ResponseEntity<ResponseData<Object>> handleConstraintViolationException(ConstraintViolationException e) {
        String message = e.getConstraintViolations().stream().map(ConstraintViolation::getMessage).collect(Collectors.joining(", "));
        ResponseData<Object> responseData = buildExceptionResponseData(message, HttpStatus.OK.value());
        log.error(ERROR_MSG_LITERAL, e);
        return ResponseEntity.ok().body(responseData);
    }

    @ExceptionHandler(value = {DatabaseException.class})
    protected ResponseEntity<ResponseData<Object>> handleException(DatabaseException e) {
        ResponseData<Object> responseData = buildExceptionResponseData(e.getMessage(), HttpStatus.OK.value());
        log.error(ERROR_MSG_LITERAL, e);
        return ResponseEntity.ok().body(responseData);
    }

    @ExceptionHandler(value = {MethodArgumentTypeMismatchException.class})
    protected ResponseEntity<ResponseData<Object>> handleException(MethodArgumentTypeMismatchException e) {
        String invalidParameterType = "Invalid parameter type";
        ResponseData<Object> responseData = buildExceptionResponseData(invalidParameterType, HttpStatus.OK.value());
        log.error(ERROR_MSG_LITERAL, e);
        return ResponseEntity.ok().body(responseData);
    }

    @ExceptionHandler(value = {RuntimeException.class})
    protected ResponseEntity<ResponseData<Object>> handleException(RuntimeException e) {
        ResponseData<Object> responseData = buildExceptionResponseData(e.getMessage(), HttpStatus.OK.value());
        log.error(ERROR_MSG_LITERAL, e);
        return ResponseEntity.ok().body(responseData);
    }

    @ExceptionHandler(value = {Exception.class})
    protected ResponseEntity<ResponseData<Object>> handleException(Exception e) {
        ResponseData<Object> responseData = buildExceptionResponseData(e.getMessage(), HttpStatus.OK.value());
        log.error(ERROR_MSG_LITERAL, e);
        return ResponseEntity.ok().body(responseData);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e, @NotNull HttpHeaders headers, @NotNull HttpStatusCode status, @NotNull WebRequest request) {
        List<String> message = e.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
        ResponseData<Object> responseData = buildExceptionResponseData(message.get(0), HttpStatus.OK.value());
        log.error(ERROR_MSG_LITERAL, e);
        return ResponseEntity.ok().body(responseData);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(@NotNull HttpMessageNotReadableException e, @NotNull HttpHeaders headers, @NotNull HttpStatusCode status, @NotNull WebRequest request) {
        String invalidRequest = "Invalid request";
        ResponseData<Object> responseData = buildExceptionResponseData(invalidRequest, HttpStatus.OK.value());
        log.error(ERROR_MSG_LITERAL, e);
        return ResponseEntity.ok().body(responseData);
    }
}