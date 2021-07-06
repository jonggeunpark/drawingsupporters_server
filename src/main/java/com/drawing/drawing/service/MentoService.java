package com.drawing.drawing.service;

import com.drawing.drawing.dto.User.LoginRequestDto;
import com.drawing.drawing.dto.Mento.MentoSignupRequestDto;
import com.drawing.drawing.entity.Mento;
import com.drawing.drawing.exception.EmailDuplicateException;
import com.drawing.drawing.exception.InvalidPasswordException;
import com.drawing.drawing.exception.NicknameDuplicateException;
import com.drawing.drawing.exception.NotFoundException;
import com.drawing.drawing.jwt.JwtFilter;
import com.drawing.drawing.jwt.TokenProvider;
import com.drawing.drawing.repository.MentoRepository;
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
public class MentoService {

    private final MentoRepository mentoRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    public Mento findOneByEmail(String email) {
        return mentoRepository.findOneByEmail(email).orElseThrow(()-> new NotFoundException("해당 이메일을 가진 유저가 없습니다."));
    }

    // 회원가입
    @Transactional
    public Mento signup(MentoSignupRequestDto mentoSignupRequestDto) {

        // 이메일 중복
        if (userRepository.findOneWithAuthoritiesByEmail(mentoSignupRequestDto.getEmail()).orElse(null) != null) {
            throw new EmailDuplicateException(mentoSignupRequestDto.getEmail());
        }

        // 닉네임 중복
        if (userRepository.findOneByNickname(mentoSignupRequestDto.getNickname()).orElse(null) != null) {
            throw new NicknameDuplicateException(mentoSignupRequestDto.getNickname());
        }

        mentoSignupRequestDto.setPassword(passwordEncoder.encode(mentoSignupRequestDto.getPassword()));
        return mentoRepository.save(mentoSignupRequestDto.toEntity());
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
