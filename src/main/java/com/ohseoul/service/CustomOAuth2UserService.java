package com.ohseoul.service;


import com.ohseoul.constant.Role;
import com.ohseoul.dto.MemberSecurityDTO;
import com.ohseoul.entity.Member;
import com.ohseoul.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.*;

import static ch.qos.logback.core.encoder.ByteArrayUtil.hexStringToByteArray;

@Service
@RequiredArgsConstructor
@Log4j2
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

  private final MemberRepository memberRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
    log.info("--------------CustomOAuth2UserService--------------");
    log.info(userRequest);

    log.info("------------------ oauth2 user------------");
    ClientRegistration clientRegistration = userRequest.getClientRegistration();
    String clientName = clientRegistration.getClientName();
    log.info("***** NAME: " + clientName);

    OAuth2User oAuth2User = super.loadUser(userRequest);
    Map<String, Object> paramMap = oAuth2User.getAttributes();
    log.info("***** ATTRIBUTES: " + paramMap);
    log.info("***** email: " + getKakaoEmail(paramMap));

//    paramMap.forEach((k, v) -> {
//      log.info("-------------paramMap---------------");
//      log.info(k + ":" + v);
//    });

    String email = null;
      if (clientName.equals("Kakao")) {
       email = getKakaoEmail(paramMap);
      }

    log.info("=========");
    log.info(email);
    log.info("=========");

    return generateDTO(email, paramMap);
  }

  private MemberSecurityDTO generateDTO(String email, Map<String, Object> paramMap) {
    Member result = memberRepository.findByEmail(email);
    String password1 = generateKey(); // 6자리 난수
    String password = passwordEncoder.encode(password1);

    log.info("서비스+++++++++++++++"+email);
    if (result == null) {
      // UUID 생성
      UUID uuid = UUID.randomUUID();

      // UUID를 16진수 문자열로 변환
      String uuidString = uuid.toString().replace("-", "");
      // Base64 인코딩
      String base64Encoded = Base64.getEncoder().encodeToString(hexStringToByteArray(uuidString));
      String nickname = "Username"+base64Encoded.substring(0,10);
      // 회원 추가
      Member member = new Member();
      member.setMemberNickname(nickname);
      member.setEmail(email);
      member.setPassword(passwordEncoder.encode(password));
      member.setSocial(true);
      member.setRole(Role.USER);
      memberRepository.save(member);

      // MemberSecurityDTO 구성 및 반환 추가
      MemberSecurityDTO memberSecurityDTO = new MemberSecurityDTO(
              email, password, true,
              Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"))
      );
      memberSecurityDTO.setProps(paramMap);
      return memberSecurityDTO;
    } else {
      String role = String.valueOf(result.getRole());
      if ("FORMER".equals(role)) {
        throw new OAuth2AuthenticationException("탈퇴한 회원입니다");
      } else {
        return new MemberSecurityDTO(
                result.getEmail(),
                result.getPassword(),
                result.isSocial(),
                Arrays.asList(new SimpleGrantedAuthority("ROLE_" + role))
        );
      }
    }
  }


  private String getKakaoEmail(Map<String, Object> paramMap) {
    log.info("------------ kakao ----------------");
    Object value = paramMap.get("kakao_account");
    log.info(value);

    LinkedHashMap accountMap = (LinkedHashMap) value;
    String email = (String)accountMap.get("email");
    log.info("--- email :" + email);
    return email;
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
