package com.grizzly.vendormicro.errorhandling;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Handles HttpMessageNotReadable exceptions thrown by calling it a malformed JSON request
     */
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException e, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String error = "Malformed JSON request";
        return buildResponse(new ApiError(HttpStatus.BAD_REQUEST, e, error));
    }

    /**
     * Handles
     */

    /**
     * Helper method to quickly build response entities from ApiErrors
     * @param apiError, the error to build a response out of
     * @return a response entity containing the ApiError
     * TODO: move this into a library
     */
    public static ResponseEntity<Object> buildResponse(ApiError apiError) {
        return new ResponseEntity<Object>(apiError, apiError.getStatus());
    }
}