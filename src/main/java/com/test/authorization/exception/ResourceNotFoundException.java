package com.test.authorization.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s not found with %s: '%s'", resourceName, fieldName, fieldValue));
    }
    public ResourceNotFoundException(Class resourceName, Object fieldValue) {
        super(String.format("%s not found with id: '%s'", resourceName.getSimpleName(), fieldValue));
    }
}
