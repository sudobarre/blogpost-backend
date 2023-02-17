package com.fede.blog.exceptions;

public class AlreadySavedException extends RuntimeException {

    public AlreadySavedException(String message) {
        super(message);
    }

}