package ua.vspelykh.usermicroservice.exception;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ResponseExceptionBody {

    private final LocalDateTime timestamp;
    private final Integer status;
    private final String message;
    private final String path;

    public ResponseExceptionBody(Integer status, String message, String path) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.message = message;
        this.path = path;
    }
}