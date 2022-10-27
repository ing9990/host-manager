package com.attoresearchhostmanager.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Taewoo
 */


@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class HostNotFoundException extends RuntimeException {
    public HostNotFoundException(String message) {
        super("Host not found: " + message);
    }
}
