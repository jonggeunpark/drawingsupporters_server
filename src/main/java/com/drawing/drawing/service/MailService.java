package com.drawing.drawing.service;

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
    private String fromAddress;

    // 메일 전송
    public void sendMail(String address, String title, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(address);
        message.setFrom(fromAddress);
        message.setSubject(title);
        message.setText(content);

        mailSender.send(message);
    }
}