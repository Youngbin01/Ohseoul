package com.ohseoul.repository;

import com.ohseoul.entity.Member;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByEmail(String email);
    Member findByMemberNickname(String membrNickname);
    @Query("SELECT m FROM Member m WHERE m.email LIKE %:email%")
    Page<Member> findByEmailContaining(@Param("email") String email, Pageable pageable);

    @Query("SELECT m FROM Member m WHERE m.memberNickname LIKE %:nickName%")
    Page<Member> findByMemberNicknameContaining(@Param("nickName") String nickName, Pageable pageable);
}
