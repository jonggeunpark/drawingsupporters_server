package com.drawing.drawing.service;

import com.drawing.drawing.entity.Drawing;
import com.drawing.drawing.entity.Feedback;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;

    @Value("${emailAddress}")
    private String address;

    @Value("${storage}")
    private String storage;

    // 메일 전송
    public void sendMail(Feedback feedback) {

        Drawing drawing = feedback.getDrawing();

        String title = "피드백이 등록되었습니다";
        String content = "피드백이 등록되었습니다.\\" +
                "요청 이메일 : " + drawing.getMentee().getEmail() + "\\" +
                "요청 id : " + drawing.getId() + "\\" +
                "요청 내용 : " + drawing.getDescription() + "\\" +
                "요청 파일 링크 : " + storage + "/" + drawing.getUuid() + drawing.getFilename();

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(address);
        message.setFrom(address);
        message.setSubject(title);
        message.setText(content);

        mailSender.send(message);
    }
}