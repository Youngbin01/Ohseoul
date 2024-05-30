package com.ohseoul.repository;

import com.ohseoul.entity.BoardCommunity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardCommunityRepository extends JpaRepository<BoardCommunity, Long>, BoardCommunityRepositoryCustom {

//    @Query("select b from BoardCommunity b where b.id = :communityNo")
//    Optional<BoardCommunity> findById(@Param("communityNo") Long communityNo);
    List<BoardCommunity> findByCreatedBy(String email);



}
