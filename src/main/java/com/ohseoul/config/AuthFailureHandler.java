package com.ohseoul.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Configuration
public class AuthFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
            throws IOException, ServletException {

        String errorMessage;
        if (exception instanceof BadCredentialsException) {
            errorMessage = "아이디 또는 비밀번호가 맞지 않습니다. 다시 확인해 주세요.";}
        else if (exception instanceof InternalAuthenticationServiceException ||
                exception instanceof OAuth2AuthenticationException) {
            errorMessage = "탈퇴한 회원입니다. 관리자에게 문의해주세요";}
        else {errorMessage = "알 수 없는 이유로 로그인에 실패하였습니다 관리자에게 문의하세요.";}

        String encodedErrorMessage = URLEncoder.encode(errorMessage, StandardCharsets.UTF_8);
        String encodedUrl = "/member/auth/login?error=true&exception=" + encodedErrorMessage;
        setDefaultFailureUrl(encodedUrl);

        super.onAuthenticationFailure(request, response, exception);
    }
}
