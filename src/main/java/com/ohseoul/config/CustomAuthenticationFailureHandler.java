package com.ohseoul.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

      if (exception instanceof InternalAuthenticationServiceException) {     // 탈퇴한 회원인 경우 처리할 작업을 여기에 작성
            // 예를 들어, 특정 페이지로 리다이렉트하거나 메시지를 출력할 수 있음
            response.sendRedirect("/withdrawn-member-error"); // 탈퇴한 회원 에러 페이지로 리다이렉트
        } else {
            // 다른 예외가 발생한 경우 기본적인 실패 처리 로직을 수행
            response.sendRedirect("/login?errordddddddd");
        }

    }
}
