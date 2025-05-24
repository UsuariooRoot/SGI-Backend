package com.uoroot.sgi.infrastructure.api.exception;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.uoroot.sgi.domain.exception.BadRequestException;
import com.uoroot.sgi.domain.exception.BusinessLogicException;
import com.uoroot.sgi.domain.exception.EmployeeNotFoundException;
import com.uoroot.sgi.domain.exception.InvalidTicketActionException;
import com.uoroot.sgi.domain.exception.InvalidTicketStateException;
import com.uoroot.sgi.domain.exception.ResourceNotFoundException;
import com.uoroot.sgi.domain.exception.TicketNotFoundException;
import com.uoroot.sgi.domain.exception.ValidationException;
import com.uoroot.sgi.infrastructure.api.dto.ApiError;

import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleResourceNotFoundException(ResourceNotFoundException ex) {
        ApiError apiError = new ApiError(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
    }
    
    @ExceptionHandler(TicketNotFoundException.class)
    public ResponseEntity<ApiError> handleTicketNotFoundException(TicketNotFoundException ex) {
        ApiError apiError = new ApiError(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
    }
    
    @ExceptionHandler(EmployeeNotFoundException.class)
    public ResponseEntity<ApiError> handleEmployeeNotFoundException(EmployeeNotFoundException ex) {
        ApiError apiError = new ApiError(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiError> handleBadRequestException(BadRequestException ex) {
        ApiError apiError = new ApiError(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }
    
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ApiError> handleValidationException(ValidationException ex) {
        ApiError apiError = ApiError.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .errors(ex.getErrors())
                .build();
                
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }

    @ExceptionHandler(BusinessLogicException.class)
    public ResponseEntity<ApiError> handleBusinessLogicException(BusinessLogicException ex) {
        ApiError apiError = new ApiError(
                HttpStatus.CONFLICT.value(),
                ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(apiError);
    }
    
    @ExceptionHandler(InvalidTicketActionException.class)
    public ResponseEntity<ApiError> handleInvalidTicketActionException(InvalidTicketActionException ex) {
        ApiError apiError = new ApiError(
                HttpStatus.CONFLICT.value(),
                ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(apiError);
    }
    
    @ExceptionHandler(InvalidTicketStateException.class)
    public ResponseEntity<ApiError> handleInvalidTicketStateException(InvalidTicketStateException ex) {
        ApiError apiError = new ApiError(
                HttpStatus.CONFLICT.value(),
                ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(apiError);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationErrors(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());

        ApiError apiError = ApiError.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message("Error de validación")
                .timestamp(LocalDateTime.now())
                .errors(errors)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiError> handleConstraintViolation(ConstraintViolationException ex) {
        List<String> errors = ex.getConstraintViolations()
                .stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .collect(Collectors.toList());

        ApiError apiError = ApiError.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message("Error de validación")
                .timestamp(LocalDateTime.now())
                .errors(errors)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiError> handleMissingParams(MissingServletRequestParameterException ex) {
        ApiError apiError = new ApiError(
                HttpStatus.BAD_REQUEST.value(),
                "Parámetro requerido faltante: " + ex.getParameterName());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiError> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
        ApiError apiError = new ApiError(
                HttpStatus.BAD_REQUEST.value(),
                "El parámetro '" + ex.getName() + "' debe ser de tipo " + 
                ex.getRequiredType().getSimpleName());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiError> handleAccessDenied(AccessDeniedException ex) {
        ApiError apiError = new ApiError(
                HttpStatus.FORBIDDEN.value(),
                "Acceso denegado: No tiene permisos para realizar esta acción");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(apiError);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGenericException(Exception ex) {
        ApiError apiError = new ApiError(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Error interno del servidor");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiError);
    }
}
