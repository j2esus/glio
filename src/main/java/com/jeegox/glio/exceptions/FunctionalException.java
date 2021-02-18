package com.jeegox.glio.exceptions;

public class FunctionalException extends RuntimeException{

    public FunctionalException(String message){
        super(message);
    }

    public FunctionalException(Exception e){
        super(e);
    }

    public FunctionalException(String message, Exception e){
        super(message, e);
    }
}
