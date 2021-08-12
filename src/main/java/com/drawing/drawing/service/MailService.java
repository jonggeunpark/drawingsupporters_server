package com.drawing.drawing.service;

import com.drawing.drawing.entity.Drawing;
import com.drawing.drawing.entity.DrawingFile;
import com.drawing.drawing.entity.Feedback;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

        List<String> filelinkList = new ArrayList<>();
        for(DrawingFile drawingFile: drawing.getDrawingFileSet()) {
            String filelink = storage + "/" + drawingFile.getUuid() + drawingFile.getFilename();
            filelinkList.add(filelink);
        }

        String title = "피드백이 등록되었습니다";
        String content = "피드백이 등록되었습니다.\n" +
                "요청 이메일 : " + drawing.getMentee().getEmail() + "\n" +
                "요청 id : " + drawing.getId() + "\n" +
                "요청 내용 : " + drawing.getDescription() + "\n" +
                "요청 파일 링크 : " + filelinkList;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(address);
        message.setFrom(address);
        message.setSubject(title);
        message.setText(content);

        mailSender.send(message);
    }
}