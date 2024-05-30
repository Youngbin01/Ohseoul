package com.ohseoul.service;


import com.ohseoul.constant.Role;
import com.ohseoul.entity.Member;
import com.ohseoul.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
@ToString
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    public Member save(Member member) {
        validateDuplicateMember(member);
        validateDuplicateMemberNickname(member);
        return memberRepository.save(member);

    }

    private void validateDuplicateMember(Member member) {
        Member findMember = memberRepository.findByEmail(member.getEmail());
        if(findMember != null) {
            throw new IllegalStateException("이미 가입된 이메일입니다.");
        }
    }

    private void validateDuplicateMemberNickname(Member member) {
        Member findMember = memberRepository.findByMemberNickname(member.getMemberNickname());
        if(findMember != null){
            throw new IllegalStateException("이미 가입된 닉네임입니다.");
        }
    }


    @Override
    public UserDetails loadUserByUsername(String MemberEmail) throws UsernameNotFoundException {

        Member member = memberRepository.findByEmail(MemberEmail);

        System.out.println("MemberEmail++++++"+MemberEmail);
        if(member == null){
            System.out.println("MemberEmail++++++sssss"+MemberEmail);
            throw new UsernameNotFoundException("회원가입을 해주세요");
        }

        else if(member.getRole() == Role.FORMER){
            throw new InternalAuthenticationServiceException("탈퇴한 회원입니다.");
        }


        return User.builder()
                .username(member.getEmail())
                .password(member.getPassword())
                .roles(member.getRole().toString())
                .build();

    }


    public void updateMemberRole(Long id, Role role) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("유저 아이디가없을때 : " + id));

        member.setRole(role);
        memberRepository.save(member);
    }
    @Transactional(readOnly = true)
    public Member getMemberById(Long id) {
        Optional<Member> optionalMember = memberRepository.findById(id);
        System.out.println(optionalMember);
        return optionalMember.orElseThrow(EntityNotFoundException::new);
    }




    //    비밀번호 찾기 수정
    public Long updatePassword(String email,String newpassword) {
        System.out.println("email-++++ updatePassword"+email);
        Member member = memberRepository.findByEmail(email);
        if (member == null) {
            throw new EntityNotFoundException("해당 이메일로 등록된 회원이 없습니다.");
        }
        member.updatePassword(newpassword, passwordEncoder);
        memberRepository.save(member);
        return member.getMemberNo();
    }













}


