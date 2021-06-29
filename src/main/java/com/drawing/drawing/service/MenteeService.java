package com.drawing.drawing.service;

import com.drawing.drawing.dto.Mentee.*;
import com.drawing.drawing.entity.Mentee;
import com.drawing.drawing.exception.InvalidPasswordException;
import com.drawing.drawing.exception.MenteeDuplicateException;
import com.drawing.drawing.exception.NotFoundException;
import com.drawing.drawing.jwt.JwtFilter;
import com.drawing.drawing.jwt.TokenProvider;
import com.drawing.drawing.repository.MenteeRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class MenteeService {

    private final MenteeRepository menteeRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    public Mentee findOneByEmail(String email) {
        return menteeRepository.findOneByEmail(email).orElseThrow(()-> new NotFoundException("해당 이메일을 가진 유저가 없습니다."));
    }

    // 회원가입
    @Transactional
    public Mentee signup(SignupRequestDto signupRequestDto) {

        // 이미 가입
        if (menteeRepository.findOneWithAuthoritiesByEmail(signupRequestDto.getEmail()).orElse(null) != null) {
            throw new MenteeDuplicateException(signupRequestDto.getEmail());
        }

        signupRequestDto.setPassword(passwordEncoder.encode(signupRequestDto.getPassword()));
        return menteeRepository.save(signupRequestDto.toEntity());
    }


    // 로그인
    @Transactional
    public HttpHeaders login(LoginRequestDto loginRequestDto) {

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginRequestDto.getEmail(), loginRequestDto.getPassword());

        HttpHeaders httpHeaders = new HttpHeaders();

        try {
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String acccessToken = tokenProvider.createJwtAccessToken(authentication);
            String refreshToken = tokenProvider.createJwtRefreshToken();

            httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer access token" + acccessToken);
            httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer refresh token" + refreshToken);

        } catch (DisabledException | LockedException | BadCredentialsException e) {
            if (e.getClass().equals(BadCredentialsException.class)) {
                throw new InvalidPasswordException(loginRequestDto.getPassword());
            }
        }

        return httpHeaders;
    }


    // 이메일 중복 확인
    public DuplicateResponseDto checkEmail(EmailDto emailDto) {

        return DuplicateResponseDto.of(menteeRepository.findOneWithAuthoritiesByEmail(emailDto.getEmail()).orElse(null) != null);
    }

    // 닉네임 중복 확인
    public DuplicateResponseDto checkNickname(NicknameDto nicknameDto) {

        return DuplicateResponseDto.of(menteeRepository.findOneByNickname(nicknameDto.getNickname()).orElse(null) != null);
    }

    // 유저 정보 확인
    public String getNickname(String email) {
        return findOneByEmail(email).getNickname();
    }

}
