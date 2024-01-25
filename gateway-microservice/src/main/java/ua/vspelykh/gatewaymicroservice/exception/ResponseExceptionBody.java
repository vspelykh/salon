package ua.vspelykh.gatewaymicroservice.exception;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ResponseExceptionBody {

    private final LocalDateTime timestamp;
    private final Integer status;
    private final String message;

    public ResponseExceptionBody(Integer status, String message) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.message = message;
    }
}