// GroupNotFoundException.java
package com.lld.splitwise.exception;

public class GroupNotFoundException extends RuntimeException {
    public GroupNotFoundException(String message) {
        super(message);
    }
}