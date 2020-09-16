package com.example.test.rest.error;

public class DeviceInUseException extends RuntimeException {
    public DeviceInUseException() {
        super("The requested device is already in use.");
    }
}
