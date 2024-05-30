package com.ohseoul.repository;

import com.ohseoul.entity.BoardNoticeImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardNoticeImgRepository extends JpaRepository<BoardNoticeImg, Long> {

    List<BoardNoticeImg> findByBoardNoticeId (Long communityNo);
}
