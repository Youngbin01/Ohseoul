package com.ohseoul.service;

import com.ohseoul.entity.Member;
import com.ohseoul.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service

@RequiredArgsConstructor
public class MyPageService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;


//    닉네임 수정
    public Long updateMemberNickname(String nickName, String email) {
        Member member = memberRepository.findByEmail(email);
        if (member == null) {
            throw new EntityNotFoundException("해당 이메일로 등록된 회원이 없습니다.");
        }
        member.updatememberNickname(nickName);
        memberRepository.save(member);
        return member.getMemberNo();

    }

//    비밀번호 수정
    public Long updatePassword(String password,String newpassword ,String email) {
        Member member = memberRepository.findByEmail(email);
        if (member == null) {
            throw new EntityNotFoundException("해당 이메일로 등록된 회원이 없습니다.");
        }
        // 현재 비밀번호 일치 여부 확인
        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new IllegalArgumentException("현재 비밀번호가 일치하지 않습니다.");
        }
        member.updatePassword(newpassword, passwordEncoder);
        memberRepository.save(member);
       return member.getMemberNo();
    }

//    회원탈퇴
    public Long memberSecession(String email) {
        Member member = memberRepository.findByEmail(email);
        if (member == null) {
            throw new EntityNotFoundException("해당 이메일로 등록된 회원이 없습니다.");
        }
        else {
            member.memberSecession(member.getRole());
            memberRepository.save(member);
            return member.getMemberNo();
        }

    }




}
