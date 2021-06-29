package com.drawing.drawing.dto.Mentee;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponseDto {
    private String access_token;
    private String refresh_token;

    public static LoginResponseDto of (String access_token, String refresh_token) {
        return new LoginResponseDto(access_token, refresh_token);
    }
}
