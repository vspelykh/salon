package ua.vspelykh.usermicroservice.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(BadCredentialsException.class)
    protected ResponseEntity<ResponseExceptionBody> handleBadCredentialsException(BadCredentialsException exception,
                                                                                  ServletWebRequest request) {
        return handleException(HttpStatus.BAD_REQUEST, exception, request);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    protected ResponseEntity<ResponseExceptionBody> handleResourceNotFoundException(ResourceNotFoundException exception,
                                                                                    ServletWebRequest request) {
        return handleException(HttpStatus.NOT_FOUND, exception, request);
    }


    private ResponseEntity<ResponseExceptionBody> handleException(HttpStatus status,
                                                                  Exception exception, ServletWebRequest request) {
        log.error(exception.getMessage());
        String path = request.getRequest().getRequestURI();
        return new ResponseEntity<>(new ResponseExceptionBody(status.value(), exception.getMessage(), path), status);
    }
}