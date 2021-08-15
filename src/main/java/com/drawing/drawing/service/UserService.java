package com.drawing.drawing.service;

import com.drawing.drawing.dto.User.DuplicateResponseDto;
import com.drawing.drawing.dto.User.EmailDto;
import com.drawing.drawing.dto.User.NicknameDto;
import com.drawing.drawing.dto.User.UserInfoResponseDto;
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

    public Boolean isMentee() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.getDtypeByEmail(authentication.getName()).equals("e");
    }

    public Boolean isMentor() {
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
    public UserInfoResponseDto getUserInfo(String email) {

        User user = findOneByEmail(email);
        String nickname = user.getNickname();

        String userType = "";

        if(isMentee()) {
            userType = "mentee";
        } else if (isMentor()) {
            userType = "mentor";
        }
        
        return UserInfoResponseDto.of(nickname, userType);
    }
}