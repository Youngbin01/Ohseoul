package com.ohseoul.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
public class EmailService {

    @Autowired
    private final JavaMailSender javaMailSender;



    public void sendEmail(String to, String key) {
        SimpleMailMessage message = new SimpleMailMessage();

        String subject = "OhSeoul 인증번호 입니다.";
        String text = "인증번호는: " + key;

        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        javaMailSender.send(message);
    }

    public String generateKey() {
        SecureRandom random = new SecureRandom();
        StringBuilder randomKey = new StringBuilder(6);

        for (int i = 0; i < 6; i++) {
            // 0 ~ 9까지 랜덤으로 6번 반복해서 randomKeyBuilder에 넣는다.
            randomKey.append(random.nextInt(10));
        }

        return randomKey.toString();
    }
}
