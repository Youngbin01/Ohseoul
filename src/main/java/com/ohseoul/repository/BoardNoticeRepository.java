package com.ohseoul.repository;

import com.ohseoul.entity.BoardNotice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardNoticeRepository extends JpaRepository<BoardNotice, Long> , BoardNoticeRepositoryCustom{

    List<BoardNotice> findByCreatedBy(String email);
}
