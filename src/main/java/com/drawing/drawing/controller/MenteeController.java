package com.drawing.drawing.controller;

import com.drawing.drawing.constants.Message;
import com.drawing.drawing.constants.ResponseMessage;
import com.drawing.drawing.constants.StatusCode;
import com.drawing.drawing.dto.Mentee.*;
import com.drawing.drawing.entity.User;
import com.drawing.drawing.exception.InvalidPasswordException;
import com.drawing.drawing.jwt.JwtFilter;
import com.drawing.drawing.jwt.TokenProvider;
import com.drawing.drawing.service.MenteeService;
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
@RequestMapping("/api/mentee")
@RequiredArgsConstructor
public class MenteeController {

    private final MenteeService menteeService;
    private final UserService userService;
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    // 멘티_회원가입
    @PostMapping("/signup")
    public ResponseEntity<Message> signup(@Valid @RequestBody SignupRequestDto signupRequestDto) {

        menteeService.signup(signupRequestDto);
        Message message = new Message(StatusCode.OK, ResponseMessage.CREATE_MENTEE);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    // 멘티_로그인
    @PostMapping("/login")
    public ResponseEntity<Message> login(@Valid @RequestBody LoginRequestDto loginRequestDto) {


        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginRequestDto.getEmail(), loginRequestDto.getPassword());

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

    // 멘티_이메일_중복_확인
    @PostMapping("/check-email")
    public ResponseEntity<Message> checkEmail(@Valid @RequestBody EmailDto emailDto) {

        DuplicateResponseDto response = menteeService.checkEmail(emailDto);

        Message message = new Message(StatusCode.OK, ResponseMessage.EMAIL_CHECK_SUCCESS, response);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    // 멘티_닉네임_중복_확인
    @PostMapping("/check-nickname")
    public ResponseEntity<Message> checkNickname(@Valid @RequestBody NicknameDto nicknameDto) {

        DuplicateResponseDto response = menteeService.checkNickname(nicknameDto);

        Message message = new Message(StatusCode.OK, ResponseMessage.NICKNAME_CHECK_SUCCESS, response);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    // 멘티_유저_정보_확인
    @GetMapping("/info")
    public ResponseEntity<Message> getNickname() {
        Authentication user = SecurityContextHolder.getContext().getAuthentication();

        Message message = new Message(StatusCode.OK, ResponseMessage.READ_USER_INFO_SUCCESS, menteeService.getNickname(user.getName()));
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
