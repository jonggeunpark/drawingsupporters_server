package com.drawing.drawing.service;

import com.drawing.drawing.dto.User.DuplicateResponseDto;
import com.drawing.drawing.dto.User.EmailDto;
import com.drawing.drawing.dto.User.NicknameDto;
import com.drawing.drawing.entity.User;
import com.drawing.drawing.exception.NotFoundException;
import com.drawing.drawing.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User findOneByEmail(String email) {
        return userRepository.findOneByEmail(email).orElseThrow(()-> new NotFoundException("해당 이메일을 가진 유저가 없습니다."));
    }

    // 필요없으면 삭제
    /*
    public User findUserByAuthentication() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return findOneByEmail(authentication.getName());
    }
     */

    public Boolean isMentee() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.getDtypeByEmail(authentication.getName()).equals("e");
    }

    public Boolean isMento() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.getDtypeByEmail(authentication.getName()).equals("o");
    }


    // 이메일 중복 확인
    public DuplicateResponseDto checkEmail(EmailDto emailDto) {

        return DuplicateResponseDto.of(userRepository.findOneWithAuthoritiesByEmail(emailDto.getEmail()).orElse(null) != null);
    }

    // 닉네임 중복 확인
    public DuplicateResponseDto checkNickname(NicknameDto nicknameDto) {

        return DuplicateResponseDto.of(userRepository.findOneByNickname(nicknameDto.getNickname()).orElse(null) != null);
    }

    // 유저 정보 확인
    public String getNickname(String email) {
        return findOneByEmail(email).getNickname();
    }


    /*
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // 회원가입
    @Transactional
    public User signup(UserDto userDto) {
        if (userRepository.findOneWithAuthoritiesByEmail(userDto.getEmail()).orElse(null) != null) {
            throw new RuntimeException("이미 가입되어 있는 유저입니다.");
        }

        Authority authority = Authority.builder()
                .authorityName("ROLE_USER")
                .build();

        User user = User.builder()
                .password(passwordEncoder.encode(userDto.getPassword()))
                .nickname(userDto.getNickname())
                .authorities(Collections.singleton(authority))
                .activated(true)
                .build();

        return userRepository.save(user);
    }

    public Optional<User> getUserWithAuthorities(String email) {
        return userRepository.findOneWithAuthoritiesByEmail(email);
    }

    public Optional<User> getMyUserWithAuthorities() {
        return SecurityUtil.getCurrentUsername().flatMap(userRepository::findOneWithAuthoritiesByEmail);
    }

     */
}