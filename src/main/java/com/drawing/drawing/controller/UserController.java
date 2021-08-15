package com.drawing.drawing.controller;


import com.drawing.drawing.constants.Message;
import com.drawing.drawing.constants.ResponseMessage;
import com.drawing.drawing.constants.StatusCode;
import com.drawing.drawing.dto.User.*;
import com.drawing.drawing.exception.InvalidPasswordException;
import com.drawing.drawing.jwt.JwtFilter;
import com.drawing.drawing.jwt.TokenProvider;
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
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    /*
    @PostMapping("/signup")
    public ResponseEntity<User> signup(
            @Valid @RequestBody UserDto userDto
    ) {
        return ResponseEntity.ok(userService.signup(userDto));
    }

    @GetMapping("/user")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<User> getMyUserInfo() {
        return ResponseEntity.ok(userService.getMyUserWithAuthorities().get());
    }

    @GetMapping("/user/{username}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<User> getUserInfo(@PathVariable String username) {
        return ResponseEntity.ok(userService.getUserWithAuthorities(username).get());
    }
    */

    /**
     * 로그인
     * METHOD : POST
     * URI : /api/user/login
     * 권한 : 없음
     */
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

    /**
     * 이메일 중복 확인
     * METHOD : POST
     * URI : /api/user/check-email
     * 권한 : 없음
     */
    @PostMapping("/check-email")
    public ResponseEntity<Message> checkEmail(@Valid @RequestBody EmailDto emailDto) {

        DuplicateResponseDto response = userService.checkEmail(emailDto);

        Message message = new Message(StatusCode.OK, ResponseMessage.EMAIL_CHECK_SUCCESS, response);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    /**
     * 닉네임 중복 확인
     * METHOD : POST
     * URI : /api/user/check-nickname
     * 권한 : 없음
     */
    @PostMapping("/check-nickname")
    public ResponseEntity<Message> checkNickname(@Valid @RequestBody NicknameDto nicknameDto) {

        DuplicateResponseDto response = userService.checkNickname(nicknameDto);

        Message message = new Message(StatusCode.OK, ResponseMessage.NICKNAME_CHECK_SUCCESS, response);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    /**
     * 유저 정보 확인 (닉네임, 유저 종류)
     * METHOD : GET
     * URI : /api/user/info
     * 권한 : 로그인
     */
    @GetMapping("/info")
    public ResponseEntity<Message> getUserInfo() {
        Authentication user = SecurityContextHolder.getContext().getAuthentication();

        UserInfoResponseDto response = userService.getUserInfo(user.getName());

        Message message = new Message(StatusCode.OK, ResponseMessage.READ_USER_INFO_SUCCESS, response);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}