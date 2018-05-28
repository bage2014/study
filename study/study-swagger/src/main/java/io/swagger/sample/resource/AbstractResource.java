package io.swagger.sample.resource;

import io.swagger.sample.exception.NotFoundException;
import io.swagger.sample.models.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

public class AbstractResource {

    @CrossOrigin(allowedHeaders = "foo", origins = "*")
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ApiResponse exception(NotFoundException e) {
        return new ApiResponse(ApiResponse.ERROR, e.getMessage());
    }
}
