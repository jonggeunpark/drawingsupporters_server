package com.drawing.drawing.dto.Mentee;

import com.drawing.drawing.entity.Authority;
import com.drawing.drawing.entity.Mentee;
import com.drawing.drawing.entity.Mento;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MenteeSignupRequestDto {

    private String email;
    private String password;
    private List<String> desired_field;
    private String nickname;
    private String job;
    private boolean marketing_agreement;
    private List<String> path_to_site_knowledge;
    private String phone_number;

    public Mentee toEntity() {

        Authority authority = Authority.builder()
                .authorityName("ROLE_USER")
                .build();

        return Mentee.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
                .activated(true)
                .authorities(Collections.singleton(authority))
                .desiredField(String.join(",", desired_field))
                .job(job)
                .marketing_yn(marketing_agreement)
                .pathToSite(String.join(",", path_to_site_knowledge))
                .phoneNumber(phone_number)
                .build();
    }
}
