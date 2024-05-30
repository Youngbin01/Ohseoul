package com.ohseoul.serviceTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.security.SecureRandom;

@SpringBootTest
public class EmailRealTest{

    @Autowired
    private JavaMailSender javaMailSender;



    @Value("${spring.mail.username}")
    private String from;

    @Test
    public void testSendEmail() throws MessagingException {
        // 테스트할 이메일 주소, 제목 및 본문
        String to = "dnemd00@naver.com";
        String subject = "OhSeoul 인증번호 입니다.";
        String text = "인증번호는 : ";

        SecureRandom random = new SecureRandom();
//		10자리의 인증번호를 생성하기 위해 10개의 스트링빌더를 생성한다.
        StringBuilder randomKey = new StringBuilder(6);


        for (int i = 0; i < 6; i++) {
//			0 ~ 9까지 랜덤으로 6번 반복해서 randomKeyBuilder에 넣는다.
            randomKey.append(random.nextInt(10));
        }

        String key = randomKey.toString();



        // MimeMessage 생성
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom(from);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text+key, true);

        // 이메일 전송
        javaMailSender.send(message);

        System.out.println("이메일이 성공적으로 전송되었습니다.");
    }
}
