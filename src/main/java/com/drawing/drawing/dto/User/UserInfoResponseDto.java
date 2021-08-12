package com.drawing.drawing.dto.User;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserInfoResponseDto {

    String nickname;
    String user_type;

    public static UserInfoResponseDto of (String nickname, String user_type) {
        return new UserInfoResponseDto(nickname, user_type);
    }
}
