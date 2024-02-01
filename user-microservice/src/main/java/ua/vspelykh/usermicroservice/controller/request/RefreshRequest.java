package ua.vspelykh.usermicroservice.controller.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RefreshRequest {

    private String refreshToken;
}
