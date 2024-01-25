package ua.vspelykh.gatewaymicroservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import reactor.core.publisher.Mono;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(JwtAuthenticationException.class)
    public Mono<ResponseExceptionBody> handleJwtAuthenticationException(JwtAuthenticationException ex) {
        return handleException(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(FeignLoginException.class)
    public Mono<ResponseExceptionBody> handleFeignLoginException(FeignLoginException ex) {
        return handleException(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    private Mono<ResponseExceptionBody> handleException(HttpStatus httpStatus, String message) {
        ResponseExceptionBody body = new ResponseExceptionBody(httpStatus.value(), message);
        return Mono.just(body);
    }
}
