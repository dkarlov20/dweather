package com.dkarlov.dweather.core.domain.exception;

public class DWeatherException extends RuntimeException {

    public DWeatherException(String message, Throwable cause) {
        super(message, cause);
    }
}
