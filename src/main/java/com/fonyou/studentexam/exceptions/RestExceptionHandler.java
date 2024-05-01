package com.fonyou.studentexam.exceptions;


import com.fonyou.studentexam.payload.response.ErrorResponse;
import com.fonyou.studentexam.payload.response.MessageResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class RestExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(RestExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<MessageResponse> handleGlobalException(Exception ex) {
        MessageResponse errorResponse = new MessageResponse("An error occurred: " + ex.getMessage());
        logger.error("Exception has been thrown", ex);
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<MessageResponse> handleBusinessException(Exception ex) {
        MessageResponse errorResponse = new MessageResponse(ex.getMessage());
        logger.info("Business Exception has been thrown", ex);
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        List<ObjectError> allErrors = ex.getBindingResult().getAllErrors();
        String errorMessages = allErrors.stream().map((DefaultMessageSourceResolvable::getDefaultMessage)).collect(Collectors.joining(", "));
        ErrorResponse message = ErrorResponse.builder().status(HttpStatus.BAD_REQUEST.value()).message(errorMessages).build();
        return ResponseEntity.badRequest().body(message);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleAccessDeniedException(Exception ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}