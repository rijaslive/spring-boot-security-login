package dev.rias.app.exception;

import dev.rias.app.vo.ErrorMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.Date;
import java.util.Locale;

/**
 * @Author Rijas
 * @Email rijas.live@gmail.com
 */
@ControllerAdvice
@Slf4j
public class ControllerExceptionHandler {

    @Autowired
    private  MessageSource messageSource;


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> globalExceptionHandler(Exception ex, WebRequest request) {
        log.error("Unexpected Error -> {}",
                messageSource.getMessage(ex.getMessage(),null,ex.getMessage(),Locale.getDefault()));
        log.error("",ex);
        String localeMessage = ex.getMessage();
        ErrorMessage message = new ErrorMessage(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                new Date(),
                localeMessage,
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                request.getDescription(false));

        return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {UsernameNotFoundException.class})
    public ResponseEntity<ErrorMessage> userNameNotFoundException(UsernameNotFoundException ex, WebRequest request) {
        log.error("UsernameNotFoundException -> {}",ex.getMessage());
        String localeMessage = ex.getMessage();
        ErrorMessage message = new ErrorMessage(
                HttpStatus.UNAUTHORIZED.value(),
                new Date(),
                localeMessage,
                HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                request.getDescription(false));

        return new ResponseEntity<>(message, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = {NoResourceFoundException.class})
    public ResponseEntity<ErrorMessage> noResourceFoundException(NoResourceFoundException ex, WebRequest request) {
        log.error("NoResourceFoundException -> {}",ex.getMessage());
        String localeMessage = ex.getMessage();
        ErrorMessage message = new ErrorMessage(
                HttpStatus.NOT_FOUND.value(),
                new Date(),
                localeMessage,
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                request.getDescription(false));

        return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
    }

}
