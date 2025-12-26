package com.learning.demo.exception;

public class CustomerNotExistsException extends RuntimeException{
    public CustomerNotExistsException(String message){
        super(message);
    }
}
