package com.drawing.drawing.controller;

import com.drawing.drawing.constants.Message;
import com.drawing.drawing.constants.ResponseMessage;
import com.drawing.drawing.constants.StatusCode;
import com.drawing.drawing.dto.Mento.MentoSignupRequestDto;
import com.drawing.drawing.dto.User.LoginRequestDto;
import com.drawing.drawing.dto.User.LoginResponseDto;
import com.drawing.drawing.exception.InvalidPasswordException;
import com.drawing.drawing.exception.NotFoundException;
import com.drawing.drawing.jwt.JwtFilter;
import com.drawing.drawing.jwt.TokenProvider;
import com.drawing.drawing.service.MentoService;
import com.drawing.drawing.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/mento")
@RequiredArgsConstructor
public class MentoController {


    private final MentoService mentoService;
    private final UserService userService;
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    // 멘토_회원가입
    @PostMapping("/signup")
    public ResponseEntity<Message> signup(@Valid @RequestBody MentoSignupRequestDto mentoSignupRequestDto) {

        mentoService.signup(mentoSignupRequestDto);
        Message message = new Message(StatusCode.OK, ResponseMessage.SIGNUP_SUCCESS);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    /*
    // 멘토_로그인
    @PostMapping("/login")
    public ResponseEntity<Message> login(@Valid @RequestBody LoginRequestDto loginRequestDto) {

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginRequestDto.getEmail(), loginRequestDto.getPassword());

        if(!userService.isMento()) throw new NotFoundException(": user type does not match");

        HttpHeaders httpHeaders = new HttpHeaders();

        try {
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String accessToken = tokenProvider.createJwtAccessToken(authentication);
            String refreshToken = tokenProvider.createJwtRefreshToken();

            httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer access token" + accessToken);
            httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer refresh token" + refreshToken);

            LoginResponseDto response = LoginResponseDto.of(accessToken, refreshToken);

            Message message = new Message(StatusCode.OK, ResponseMessage.LOGIN_SUCCESS, response);
            return new ResponseEntity<>(message, httpHeaders, HttpStatus.OK);

        } catch (DisabledException | LockedException | BadCredentialsException e) {
            if (e.getClass().equals(BadCredentialsException.class)) {
                throw new InvalidPasswordException(loginRequestDto.getPassword());
            }
        }

        Message message = new Message(StatusCode.OK, ResponseMessage.LOGIN_SUCCESS);
        return new ResponseEntity<>(message, httpHeaders, HttpStatus.OK);
    }
    */
}
