package com.grizzly.grizlibrary.errorhandling;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

abstract class ApiSubError {}

@Data
@EqualsAndHashCode
@AllArgsConstructor
class ApiValidationError extends ApiSubError {
    private String object; // the entity that had an issue
    private String field; // the field that failed validation
    private Object rejectedValue; // the value that was given by the client
    private String message; // error message based on the validation failure

    ApiValidationError(String object, String message) {
        this.object = object;
        this.message = message;
    }
}
