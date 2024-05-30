package com.ohseoul.repository;

import com.ohseoul.entity.BoardCommunityReply;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardCommunityReplyRepository extends JpaRepository<BoardCommunityReply, Long> {

    List<BoardCommunityReply> findByBoardCommunityId(Long boardCommunityId);
    BoardCommunityReply findByCommunityReplyNo(@Param("replyId") Long replyId);
}
