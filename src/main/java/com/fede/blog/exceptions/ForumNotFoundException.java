package com.fede.blog.exceptions;

public class ForumNotFoundException  extends RuntimeException {
    public ForumNotFoundException(String message) {
        super(message);
    }
}