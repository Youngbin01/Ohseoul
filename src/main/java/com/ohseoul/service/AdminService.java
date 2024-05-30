package com.ohseoul.service;

import com.ohseoul.dto.BoardSearchDTO;
import com.ohseoul.dto.MemberFormDTO;
import com.ohseoul.entity.BoardCommunity;
import com.ohseoul.entity.EventInfo;
import com.ohseoul.entity.Member;
import com.ohseoul.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminService {

//   전체 회원정보 조히
    private final MemberRepository memberRepository;
    public Page<Member> getAllMembers(Pageable pageable) {
        return memberRepository.findAll(pageable);
    }

    public Page<Member> getAllMembers(Pageable pageable, String nickName) {
        return memberRepository.findByMemberNicknameContaining(nickName, pageable);
//        return memberRepository.findByEmailContaining(email, pageable);
    }


}



