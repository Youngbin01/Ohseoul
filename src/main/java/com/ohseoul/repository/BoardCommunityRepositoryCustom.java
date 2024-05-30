package com.ohseoul.repository;

import com.ohseoul.dto.BoardSearchDTO;
import com.ohseoul.entity.BoardCommunity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardCommunityRepositoryCustom {

    Page<BoardCommunity> getBoardListPage(BoardSearchDTO boardSearchDTO, Pageable pageable);


}
