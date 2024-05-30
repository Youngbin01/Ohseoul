package com.ohseoul.repository;

import com.ohseoul.entity.BoardNoticeReply;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardNoticeReplyRepository extends JpaRepository<BoardNoticeReply,Long> {

    List<BoardNoticeReply> findByBoardNoticeId(Long boardCommunityId);
    BoardNoticeReply findByNoticeReplyNo(@Param("replyId") Long replyId);
}
