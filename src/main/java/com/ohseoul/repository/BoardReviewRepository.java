package com.ohseoul.repository;

import com.ohseoul.entity.BoardReview;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardReviewRepository extends JpaRepository<BoardReview, Long> {
    List<BoardReview> findByEventInfoEventId(Long id);
    BoardReview findByReviewNo(@Param("reviewId") Long reviewId);
}
