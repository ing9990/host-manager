package com.attoresearchhostmanager.exception;

/**
 * @author Taewoo
 */


public class HostNotFoundException extends RuntimeException {
    public HostNotFoundException(String message) {
        super("Host not found: " + message);
    }
}
