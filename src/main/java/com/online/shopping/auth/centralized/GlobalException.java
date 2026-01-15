package com.online.shopping.auth.centralized;

import com.online.shopping.auth.dto.ErrorResponse;
import com.online.shopping.auth.exception.UserAlreadyExist;
import com.online.shopping.auth.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalException {

    @ExceptionHandler(UserAlreadyExist.class)
    @ResponseStatus(HttpStatus.ALREADY_REPORTED)
    public @ResponseBody ErrorResponse handleUserAlreadyExistException(Exception e){
        return new ErrorResponse(HttpStatus.ALREADY_REPORTED,e.getMessage()) ;
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public @ResponseBody ErrorResponse handleUserNotFoundException(Exception e){
        return new ErrorResponse(HttpStatus.NOT_FOUND,e.getMessage()) ;
    }
}
