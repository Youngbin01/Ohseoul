package com.ohseoul.controller;


import com.ohseoul.dto.MemberFormDTO;
import com.ohseoul.entity.Member;
import com.ohseoul.repository.MemberRepository;
import com.ohseoul.service.CustomOAuth2UserService;
import com.ohseoul.service.EmailService;
import com.ohseoul.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Controller
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
//    private final CustomOAuth2UserService oAuthService;
    private final MemberRepository memberRepository;
    private String key;
    // key와 해당 유효시간을 저장할 맵
    private Map<String, LocalDateTime> keyExpirationMap = new HashMap<>();

    //     회원가입
    @PostMapping("/join")
    public String join(@Valid @RequestBody MemberFormDTO memberFormDTO,
                       BindingResult bindingResult, Model model) {
        System.out.println("memberDTO+++++++"+ memberFormDTO);
        if (bindingResult.hasErrors()) {
            System.out.println("에러발생 -+-++"+ memberFormDTO);
            return "member/join";
        }
        try {
            Member member = Member.createMember(memberFormDTO, passwordEncoder);
            System.out.println("memberDTO+++++++"+member);
            memberService.save(member);
        } catch (IllegalStateException e){
            model.addAttribute("errorMessage", e.getMessage());
        }
        return "member/join";
    }


    //    로그인 시도 시큐리티에서 처리
    @PostMapping("/login")
    public String login(@Valid MemberFormDTO memberFormDTO){
        return "/";
    }
    //  이메일 중복체크
    @PostMapping("/emailCheck")
    public ResponseEntity<String> checkEmail(@RequestBody Map<String, String> requestData) {
        String email = requestData.get("email");
        Member findMember = memberRepository.findByEmail(email);
        if (findMember != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 가입된 이메일입니다.");
        }
        else if (email == null || email.isEmpty() || !email.contains("@")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이메일 형식으로 작성해주세요");
        }
        else {
            return ResponseEntity.ok("사용가능한 이메일입니다.");
        }
    }

    //    닉네임 중복체크
    @PostMapping("/nickNameCheck")
    public ResponseEntity<String> checkNickName(@RequestBody Map<String, String> requestData) {
        Member findMember = memberRepository.findByMemberNickname(requestData.get("nickName"));
        if (findMember != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이미 가입된 닉네임입니다.");
        } else {
            return ResponseEntity.ok("사용가능한 닉네임입니다..");
        }
    }






    //    인증번호 발송
    @PostMapping("/key")
    public ResponseEntity<Void> kendVerificationCode(@RequestBody Map<String, String> requestData) {
        String email = requestData.get("email");
        System.out.println("email: " + email);
        key = emailService.generateKey(); // 키 생성
        System.out.println("key: " + key);
        // 유효시간 설정
        LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(3); // 예시로 3분 설정
        keyExpirationMap.put(key, expirationTime);

        // 이메일 발송
        emailService.sendEmail(email, key);

        return ResponseEntity.ok().build();
    }

    //    이메일 인증번호 일치 여부
    @PostMapping("/verify")
    public  ResponseEntity<Void>  match(@RequestBody Map<String, String> requestData){
        String keymatch = requestData.get("verificationCode");
        System.out.println("keymatch: " + keymatch);
        System.out.println("key: " + key);
        if(!keymatch.equals(key)){return ResponseEntity.badRequest().build();}
        else {return ResponseEntity.ok().build();}
    }











//  일반 회원 로그인 실패 핸들러
    @GetMapping("/auth/login")public String login(@RequestParam(value = "error", required = false)String error
            ,@RequestParam(value = "exception", required = false)String exception,Model model) {
        System.out.println("컨트롤러 왔어요 로그인 실패");
        model.addAttribute("error", error);
        model.addAttribute("exception", exception);
        return "/member/login";}


//    @GetMapping("/member/login?error")
//    public String loginWithError(Model model, @RequestParam(name = "error", required = false) String error) {
//        if (error != null) {
//           model.addAttribute("error", "사용자는 로그인 권한이 없습니다.");
//           return "/";
//        }
//        else {
//        model.addAttribute("error", "사용자는 로그인 권한이 없습니다.");
//            return "/";
//        }
//    }

    @GetMapping("/member/login?error")
    public String loginWithError() {
        return "/";
    }

//      회원가입 이동
    @GetMapping("/join")
    public String loginform() {
        return "member/join";
    }

//    로그인 페이지 이동
    @GetMapping("/login")
    public String login() {
        return "member/login";
    }

// 비밀번호 찾기 이동
    @GetMapping("/findpassword")
    public String findPassword() {
        return "member/findpassword";
    }


// 비밀번호 수정
    @PostMapping("/passwordmodify")
    public ResponseEntity<Void> passwordmodify(@RequestBody Map<String, String> requestData,
                                               BindingResult bindingResult) {
        String email = requestData.get("email");
        System.out.println("컨트롤러`````"+email);
        String password = requestData.get("password");
        if(bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        if(memberRepository.findByEmail(email) != null) {
            memberService.updatePassword(email, password);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();

    }
    
    //  이메일 찾기
    @PostMapping("/emailconfirm")
    public ResponseEntity<String> emailconfirm(@RequestBody Map<String, String> requestData) {
        Member findMember = memberRepository.findByEmail(requestData.get("email"));
        if (findMember != null) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }



}
