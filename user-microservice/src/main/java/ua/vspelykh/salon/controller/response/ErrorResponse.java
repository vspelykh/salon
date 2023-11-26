package ua.vspelykh.salon.controller.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
public class ErrorResponse {

    private HttpStatus httpStatus;
    private String message;
}