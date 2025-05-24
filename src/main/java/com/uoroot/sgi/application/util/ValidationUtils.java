package com.uoroot.sgi.application.util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import com.uoroot.sgi.domain.exception.ValidationException;

/**
 * Utility class for validation operations in the service layer.
 */
public class ValidationUtils {

    /**
     * Validates that an object is not null.
     * 
     * @param object The object to validate
     * @param message The error message if validation fails
     * @throws ValidationException if the object is null
     */
    public static void validateNotNull(Object object, String message) {
        if (object == null) {
            throw new ValidationException(message);
        }
    }
    
    /**
     * Validates that a string is not blank (null, empty, or whitespace only).
     * 
     * @param text The string to validate
     * @param message The error message if validation fails
     * @throws ValidationException if the string is blank
     */
    public static void validateNotBlank(String text, String message) {
        if (text == null || text.trim().isEmpty()) {
            throw new ValidationException(message);
        }
    }
    
    /**
     * Validates that a condition is true.
     * 
     * @param condition The condition to validate
     * @param message The error message if validation fails
     * @throws ValidationException if the condition is false
     */
    public static void validateCondition(boolean condition, String message) {
        if (!condition) {
            throw new ValidationException(message);
        }
    }
    
    /**
     * Validates multiple conditions and collects all validation errors.
     * 
     * @param validations List of validation suppliers that return error messages or null if valid
     * @param errorMessage The general error message for the validation exception
     * @throws ValidationException if any validation fails, containing all error messages
     */
    public static void validateAll(List<Supplier<String>> validations, String errorMessage) {
        List<String> errors = new ArrayList<>();
        
        for (Supplier<String> validation : validations) {
            String error = validation.get();
            if (error != null) {
                errors.add(error);
            }
        }
        
        if (!errors.isEmpty()) {
            throw new ValidationException(errorMessage, errors);
        }
    }
}
