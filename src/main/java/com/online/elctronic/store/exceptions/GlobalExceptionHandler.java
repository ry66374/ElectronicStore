package com.online.elctronic.store.exceptions;


import com.online.elctronic.store.dtos.ApiResponseMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    //handle resource not found exception
    private Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponseMessage> resourceNotFoundExceptionHandler(ResourceNotFoundException ex){

        logger.info("Exception Handler Invoked");
        ApiResponseMessage responseMessage = ApiResponseMessage
                .builder()
                .message(ex.getMessage())
                .status(HttpStatus.NOT_FOUND)
                .success(true)
                .build();

        return new ResponseEntity<>(responseMessage,HttpStatus.NOT_FOUND);
    }

    // Method Argument is  not VALID Exception
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){

        List<ObjectError> allError = ex.getBindingResult().getAllErrors();
        Map<String, Object> response = new HashMap<>();
        allError.stream().forEach(objectError -> {
            String message = objectError.getDefaultMessage();
            String field = ((FieldError) objectError).getField();
            response.put(field,message);
        });

        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    };


    @ExceptionHandler(BadApiRequest.class)
    public ResponseEntity<ApiResponseMessage> handleBadApiRequest(BadApiRequest ex){

        logger.info("Bad Api request");
        ApiResponseMessage responseMessage = ApiResponseMessage
                .builder()
                .message(ex.getMessage())
                .status(HttpStatus.NOT_FOUND)
                .success(false)
                .build();

        return new ResponseEntity<>(responseMessage,HttpStatus.BAD_REQUEST);
    }

}
