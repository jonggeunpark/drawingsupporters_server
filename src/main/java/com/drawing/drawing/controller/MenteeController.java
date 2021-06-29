
    // 멘티_이메일_중복_확인
    @GetMapping("/check-email")
    public ResponseEntity<Message> checkEmail(@Valid @RequestBody EmailDto emailDto) {

        DuplicateResponseDto response = menteeService.checkEmail(emailDto);

        Message message = new Message(StatusCode.OK, ResponseMessage.EMAIL_CHECK_SUCCESS, response);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    // 멘티_닉네임_중복_확인
    @GetMapping("/check-nickname")
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
