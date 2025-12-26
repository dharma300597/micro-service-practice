package com.learning.demo.exception;

public class CustomerAlreadyExistsException  extends RuntimeException{
    public CustomerAlreadyExistsException(String message){
        super(message);
    }
}
