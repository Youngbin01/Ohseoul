package com.ohseoul.entity;

import com.ohseoul.constant.Role;
import com.ohseoul.dto.MemberFormDTO;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import javax.validation.Valid;

@Entity
@Getter
@Setter
public class Member extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberNo;
    private String password;
    @Column(unique = true)
    private String email;
    private String memberNickname;
    private boolean social;
    @Enumerated(EnumType.STRING)
    private Role role;



    public static Member createMember(@Valid MemberFormDTO memberFormDTO,
                                      PasswordEncoder passwordEncoder){

        System.out.println("createMember member엔티티 들어옴++ : "+memberFormDTO);
        Member member = new Member();
        member.setEmail(memberFormDTO.getEmail());
        member.setMemberNickname(memberFormDTO.getNickName());
        String password = passwordEncoder.encode(memberFormDTO.getPassword());
        System.out.println("createMember password : "+password);
        member.setPassword(password);
        member.setRole(Role.USER);

        return member;

    }

    public void updatememberNickname(String memberNickname) {
        this.memberNickname = memberNickname;
    }

    public void updatePassword(String password,PasswordEncoder passwordEncoder) {
        String password1 = passwordEncoder.encode(password);
        this.password = password1;
    }

    public void memberSecession(Role role){
        Role seccessionRole = role.FORMER;
        this.role = seccessionRole;
    }


}


