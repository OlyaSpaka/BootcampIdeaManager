package com.example.demo.exceptions;

public class UsernameTakenException extends Exception {
    public UsernameTakenException(String msg) {
        super(msg);
    }
}
