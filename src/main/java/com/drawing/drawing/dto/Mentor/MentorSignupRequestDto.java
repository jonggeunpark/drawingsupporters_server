package com.drawing.drawing.dto.Mentor;

import com.drawing.drawing.entity.Authority;
import com.drawing.drawing.entity.Mentor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collections;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MentorSignupRequestDto {

    private String email;
    private String password;
    private String nickname;
    private String phone_number;

    public Mentor toEntity() {

        Authority authority = Authority.builder()
                .authorityName("ROLE_USER")
                .build();

        return Mentor.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
                .activated(true)
                .authorities(Collections.singleton(authority))
                .phoneNumber(phone_number)
                .build();

    }
}
