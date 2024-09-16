package com.example.demo.exceptions;

public class EmailTakenException extends Exception {
    public EmailTakenException(String msg) {
        super(msg);
    }
}
