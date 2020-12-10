package com.jeegox.glio.exceptions;

public class BusinessException extends RuntimeException{

    public BusinessException(String message){
        super(message);
    }

    public BusinessException(Exception e){
        super(e);
    }

    public BusinessException(String message, Exception e){
        super(message, e);
    }
}
