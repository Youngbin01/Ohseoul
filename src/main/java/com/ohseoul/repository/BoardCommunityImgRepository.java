package com.ohseoul.repository;

import com.ohseoul.entity.BoardCommunityImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardCommunityImgRepository extends JpaRepository<BoardCommunityImg, Long> {

   List<BoardCommunityImg> findByBoardCommunityId(Long communityNo);



}
