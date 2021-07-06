package com.drawing.drawing.dto.Mento;

import com.drawing.drawing.entity.Authority;
import com.drawing.drawing.entity.Mento;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class MentoSignupRequestDto {

    private String email;
    private String password;
    private String nickname;
    private String phone_number;

    public Mento toEntity() {

        Authority authority = Authority.builder()
                .authorityName("ROLE_USER")
                .build();

        return Mento.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
                .activated(true)
                .authorities(Collections.singleton(authority))
                .phoneNumber(phone_number)
                .build();

    }
}
