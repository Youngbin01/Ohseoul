package com.ohseoul.repository;

import com.ohseoul.dto.NoticeSearchDTO;
import com.ohseoul.entity.BoardNotice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardNoticeRepositoryCustom {

    Page<BoardNotice> getNoticeListPage(NoticeSearchDTO boardSearchDTO, Pageable pageable);
}
