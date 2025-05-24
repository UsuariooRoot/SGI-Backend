package com.uoroot.sgi.infrastructure.api.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.uoroot.sgi.infrastructure.api.dto.ApiError;
import com.uoroot.sgi.infrastructure.api.dto.ApiResponse;

import java.util.List;

/**
 * Utility class for building standardized API responses.
 */
public class ResponseBuilder {

    /**
     * Creates a success response with the given data.
     *
     * @param <T>  The type of the data
     * @param data The data to include in the response
     * @return ResponseEntity with the data wrapped in an ApiResponse
     */
    public static <T> ResponseEntity<ApiResponse<T>> success(T data) {
        return ResponseEntity.ok(new ApiResponse<>(data, getCount(data)));
    }

    /**
     * Creates a success response with the given data and custom count.
     *
     * @param <T>   The type of the data
     * @param data  The data to include in the response
     * @param count The count to include in the response
     * @return ResponseEntity with the data wrapped in an ApiResponse
     */
    public static <T> ResponseEntity<ApiResponse<T>> success(T data, int count) {
        return ResponseEntity.ok(new ApiResponse<>(data, count));
    }

    /**
     * Creates an error response with the given status and message.
     *
     * @param status  The HTTP status code
     * @param message The error message
     * @return ResponseEntity with an ApiError
     */
    public static ResponseEntity<ApiError> error(HttpStatus status, String message) {
        ApiError apiError = new ApiError(status.value(), message);
        return ResponseEntity.status(status).body(apiError);
    }

    /**
     * Creates an error response with the given status, message, and error details.
     *
     * @param status  The HTTP status code
     * @param message The error message
     * @param errors  List of detailed error messages
     * @return ResponseEntity with an ApiError
     */
    public static ResponseEntity<ApiError> error(HttpStatus status, String message, List<String> errors) {
        ApiError apiError = ApiError.builder()
                .status(status.value())
                .message(message)
                .errors(errors)
                .build();
        return ResponseEntity.status(status).body(apiError);
    }

    /**
     * Gets the count of items in the data.
     * If data is a List, returns its size.
     * If data is a single object, returns 1.
     * If data is null, returns 0.
     *
     * @param <T>  The type of the data
     * @param data The data to count
     * @return The count of items
     */
    private static <T> int getCount(T data) {
        if (data == null) {
            return 0;
        }
        if (data instanceof List) {
            return ((List<?>) data).size();
        }
        return 1;
    }
}
