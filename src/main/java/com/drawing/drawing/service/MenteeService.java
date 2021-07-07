package com.drawing.drawing.service;

import com.drawing.drawing.dto.Mentee.*;
import com.drawing.drawing.dto.User.LoginRequestDto;
import com.drawing.drawing.entity.Feedback;
import com.drawing.drawing.entity.Mentee;
import com.drawing.drawing.exception.EmailDuplicateException;
import com.drawing.drawing.exception.InvalidPasswordException;
import com.drawing.drawing.exception.NicknameDuplicateException;
import com.drawing.drawing.exception.NotFoundException;
import com.drawing.drawing.jwt.JwtFilter;
import com.drawing.drawing.jwt.TokenProvider;
import com.drawing.drawing.repository.MenteeRepository;
import com.drawing.drawing.repository.UserRepository;
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
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    public Mentee findOneByEmail(String email) {
        return menteeRepository.findOneByEmail(email).orElseThrow(()-> new NotFoundException("해당 이메일을 가진 유저가 없습니다."));
    }

    @Transactional
    public Long saveMentee(Mentee mentee) {
        return menteeRepository.save(mentee).getId();
    }

    // 회원가입
    @Transactional
    public void signup(MenteeSignupRequestDto menteeSignupRequestDto) {

        // 이메일 중복
        if (userRepository.findOneWithAuthoritiesByEmail(menteeSignupRequestDto.getEmail()).orElse(null) != null) {
            throw new EmailDuplicateException(menteeSignupRequestDto.getEmail());
        }

        // 닉네임 중복
        if (userRepository.findOneByNickname(menteeSignupRequestDto.getNickname()).orElse(null) != null) {
            throw new NicknameDuplicateException(menteeSignupRequestDto.getNickname());
        }

        menteeSignupRequestDto.setPassword(passwordEncoder.encode(menteeSignupRequestDto.getPassword()));

        saveMentee(menteeSignupRequestDto.toEntity());
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

}
