package com.ohseoul.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthFailureHandler authFailureHandler;

    private PasswordEncoder passwordEncoder;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        System.out.println("----------------- SecurityFilterChain ----------------");

//        http.csrf().disable();
        http.formLogin()
                // 로그인 처리 화면
                .loginPage("/member/login")
                .defaultSuccessUrl("/")
                .usernameParameter("email")   //loadUserByUsername(String email)로 실행
                .failureHandler(authFailureHandler)
                .and()
                .logout()
                // 로그아웃 처리 url
                .logoutRequestMatcher(new AntPathRequestMatcher("/members/logout"))
                .logoutSuccessUrl("/");

//    permitAll(): 모든 사용자가 인증(로그인) 없이 해당 경로에 접근 가능
//    hasRole("ADMIN"): 관리자인 경우 /admin/으로 접근하는 경로를 통과시킴
//    anyRequest().authenticated() 위의 경우 이외의 페이지는 인증 절차가 필요.

        http
                .authorizeRequests()
                // 비회원들도 기본적인 게시글 조회 및 메인컨텐츠 조회 가능
                .mvcMatchers("/","error", "favicon.io","/schedule/**","/notice/**","/community/**",
                        "/member/**","/search/**").permitAll()
                // user들 에게는 게시글 작성 수정 삭제 기능에 추가
                .mvcMatchers("/user/**","/mypage/**","/chat/**").hasAnyRole("USER","ADMIN")
                // 공지사항 게시판 작성 수정 삭제 등록, 및 모든 게시판 삭제
                .mvcMatchers("/admin/**","/community/admin/**").hasRole("ADMIN")
                // 나머지 모든 요청은 인증된 사용자에게만 허용
                .anyRequest().authenticated();
        //카카오 소셜 로그인 했을때 우리 사이트에 가입시킬지 물어보는거
        http.oauth2Login().loginPage("/member/login");

//    인증되지 않은 사용자가 리소스 접근하여 실패했을 때 처리하는 핸들러 등록
        http.exceptionHandling()
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint());

        return http.build();

    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    //  /resource/static 폴더의 하위 파일은 인증에서 제외시킴.
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        return (web)-> web.ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }
    @Bean
    public AuthenticationSuccessHandler customSuccessHandler(){
        return new CustomSocialLoginSuccessHandler(passwordEncoder);
    }

}