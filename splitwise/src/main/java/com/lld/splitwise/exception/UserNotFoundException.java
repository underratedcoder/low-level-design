// UserNotFoundException.java
package com.lld.splitwise.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
