package com.online.shopping.auth.exception;

public class UserAlreadyExist extends  RuntimeException{


    public UserAlreadyExist(String message){
        super(message);
    }
}
