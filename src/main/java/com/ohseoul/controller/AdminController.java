package com.ohseoul.controller;

import com.ohseoul.constant.Role;
import com.ohseoul.dto.BoardCommunityDTO;
import com.ohseoul.dto.MemberFormDTO;
import com.ohseoul.entity.Member;
import com.ohseoul.service.AdminService;
import com.ohseoul.service.BoardCommunityService;
import com.ohseoul.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.List;
@Controller
public class AdminController {
    @Autowired
    MemberService memberService;

    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    AdminService adminService;
    @Autowired
    BoardCommunityService boardCommunityService;

    @GetMapping({"/admin/list/{requestData}", "/admin/list/"})
    public String list(Model model,
                       @RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "10") int size,
                       String nickName) {

        Page<Member> membersPage = null;

        Pageable pageable = PageRequest.of(page, size);
        if(nickName == null) {
            membersPage = adminService.getAllMembers(pageable);
            // 페이지 정보와 회원 목록을 모델에 추가합니다.
            model.addAttribute("membersPage", membersPage);

        } else{
            membersPage = adminService.getAllMembers(pageable, nickName);
        }

        //BoardService에서 만들어준 boardList가 반환되는데, list라는 이름으로 받아서 넘기겠다는 뜻
        model.addAttribute("membersPage" , membersPage);
        return "admin/list";
    }



    @GetMapping("/admin/newad")
    public String adminForm(Model model) {

        model.addAttribute("memberDTO", new MemberFormDTO());
        return "admin/adminForm";
    }

    @PostMapping("/admin/newad")
    public String newAdmin(@Valid MemberFormDTO memberFormDTO,
                           BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "admin/adminForm";
        }
        try {
            Member member = Member.createMember(memberFormDTO, passwordEncoder);
            memberService.save(member);
        } catch (IllegalStateException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "admin/adminForm";
        }
        return "redirect:/";
    }
    @GetMapping("/admin/view")
    public String getMemberById(@RequestParam("memberNo") Long memberNo, Model model) {
        Member member = memberService.getMemberById(memberNo);
        String email = member.getEmail();
        List<BoardCommunityDTO> boardCommunityDTOList = boardCommunityService.myBoardCommunity(email);
        System.out.println("myBoardCommunityDTOList: 컨트롤러"+boardCommunityDTOList);
        model.addAttribute("boardCommunityDTOList", boardCommunityDTOList);
        model.addAttribute("member", member);
        return "admin/view";
    }
    @PostMapping("/admin/modify")
    public String updateMemberRole(@RequestParam("memberNo") Long memberNo, Role role) {
        memberService.updateMemberRole(memberNo, role);
        return "redirect:/admin/view?memberNo="+memberNo;
    }

    @GetMapping("/admin/modify")
    public String getMemberByIdModify(@RequestParam("memberNo") Long memberNo, Model model) {
        Member member = memberService.getMemberById(memberNo);
        model.addAttribute("member", member);
        return "admin/modify";
    }




}
