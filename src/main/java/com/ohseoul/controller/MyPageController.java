package com.ohseoul.controller;

import com.ohseoul.dto.BoardCommunityDTO;
import com.ohseoul.entity.Member;
import com.ohseoul.repository.MemberRepository;
import com.ohseoul.service.BoardCommunityService;
import com.ohseoul.service.MyPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class MyPageController {

    private final MemberRepository memberRepository;
    private final MyPageService myPageService;
    private final BoardCommunityService boardCommunityService;

    @GetMapping("/mypage")
    public String modify(Principal principal, Model model) {
        String email = principal.getName();
        // 해당 이메일로 회원 정보를 조회합니다.
        Member member = memberRepository.findByEmail(email);


            model.addAttribute("social", member.isSocial());
            model.addAttribute("nickName", member.getMemberNickname());
            model.addAttribute("title", "MyPage");

        return "mypage/profile";
    }
    @GetMapping("/mypage/passwordmodify")
    public String passwordmodify(Principal principal, Model model) {
        String email = principal.getName();
        // 해당 이메일로 회원 정보를 조회합니다.
        Member member = memberRepository.findByEmail(email);
        model.addAttribute("nickName",member.getMemberNickname());
        model.addAttribute("title","마이페이지");
        model.addAttribute("social", member.isSocial());

        return "mypage/password_change";
    }

    @PostMapping("/mypage/modify")
    public String modify(@RequestBody Map<String, String> requestData, BindingResult bindingResult, Principal principal) {
        String email = principal.getName();
        System.out.println("컨트롤러+++++++++++"+email);
        // 해당 이메일로 회원 정보를 조회합니다.
        Member member = memberRepository.findByEmail(email);
        // 회원 정보가 없는 경우 예외 처리를 추가할 수 있습니다.
        if (bindingResult.hasErrors()) {
            System.out.println("에러발생 -+-++"+ requestData);
            return "/mypage";
        }
        if (member == null) {
            // 예외 처리
            return "redirect:/error";
        }
        // 요청 데이터에서 닉네임을 가져옵니다.
        String nickName = requestData.get("nickName");
        System.out.println("컨트롤러+++++++++++"+nickName);
        // 회원의 닉네임을 업데이트합니다.
        myPageService.updateMemberNickname(nickName, email);
        // 홈페이지로 리다이렉트합니다.
        return "redirect:/";
    }

    @PostMapping("/mypage/passwordmodify")
    public ResponseEntity<String> passwordModify(@RequestBody Map<String, String> requestData,
                                                 BindingResult bindingResult, Principal principal) {
        String email = principal.getName();
        System.out.println("컨트롤러+++++++++++"+email);

        // 해당 이메일로 회원 정보를 조회합니다.
        Member member = memberRepository.findByEmail(email);
        if (member == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 이메일로 등록된 회원이 없습니다.");
        }
        if(!requestData.get("password").equals(requestData.get("password1"))){
            System.out.println("password");
            return ResponseEntity.badRequest().body("새로운 비밀번호와 확인용 비밀번호가 일치하지 않습니다.");
        }
        // 비밀번호 변경
        String password = requestData.get("password");
        try {
            myPageService.updatePassword(requestData.get("basicpassword"),password ,email);
            return ResponseEntity.ok("비밀번호가 성공적으로 수정되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("현재 비밀번호가 일치하지 않습니다.");
        }
    }

    @GetMapping("/mypage/secession")
    public String secessionPage(Principal principal, Model model) {
        String email = principal.getName();
        Member member = memberRepository.findByEmail(email);
        model.addAttribute("nickName", member.getMemberNickname());
        model.addAttribute("title", "마이페이지");
        model.addAttribute("social", member.isSocial());

        return "mypage/secession";
    }
    @PostMapping("/mypage/secession")
    public String secession(Principal principal, HttpServletRequest request, HttpServletResponse response) {
        String email = principal.getName();
        Member member = memberRepository.findByEmail(email);
        if(member != null){
            myPageService.memberSecession(email);
            // Logout the user
            new SecurityContextLogoutHandler().logout(request, response, null);
        }

        return "redirect:/";
    }

    @GetMapping("/mypage/board")
    public String myBoard(Principal principal, Model model) {
        String email = principal.getName();
        Member member = memberRepository.findByEmail(email);
        List<BoardCommunityDTO> boardCommunityDTOList = boardCommunityService.myBoardCommunity(email);
        System.out.println("myBoardCommunityDTOList: 컨트롤러"+boardCommunityDTOList);

        model.addAttribute("social", member.isSocial());
        model.addAttribute("boardCommunityDTOList", boardCommunityDTOList);
        model.addAttribute("nickName", member.getMemberNickname());

        return "mypage/board";
    }



}
