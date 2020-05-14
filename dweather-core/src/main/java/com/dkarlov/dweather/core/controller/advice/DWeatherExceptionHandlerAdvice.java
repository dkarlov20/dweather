package com.dkarlov.dweather.core.controller.advice;

import com.dkarlov.dweather.core.domain.dto.DWeatherErrorResponse;
import com.dkarlov.dweather.core.domain.exception.DWeatherException;
import com.dkarlov.dweather.core.domain.exception.DataNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

import static java.time.format.DateTimeFormatter.ISO_DATE;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
@Slf4j
public class DWeatherExceptionHandlerAdvice {

    @ExceptionHandler({DataNotFoundException.class})
    @ResponseStatus(NOT_FOUND)
    public DWeatherErrorResponse handleDataNotFoundException(DataNotFoundException e) {
        log.error(e.getMessage());
        return DWeatherErrorResponse.builder()
                .message(e.getMessage())
                .createdTime(LocalDateTime.now().format(ISO_DATE))
                .build();
    }

    @ExceptionHandler({DWeatherException.class})
    @ResponseStatus(BAD_REQUEST)
    public DWeatherErrorResponse handleDWeatherException(DWeatherException e) {
        log.error(e.getMessage());
        return DWeatherErrorResponse.builder()
                .message(e.getMessage())
                .createdTime(LocalDateTime.now().format(ISO_DATE))
                .build();
    }
}
