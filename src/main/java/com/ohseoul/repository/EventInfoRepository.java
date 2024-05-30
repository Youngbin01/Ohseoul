package com.ohseoul.repository;

import com.ohseoul.entity.EventInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EventInfoRepository extends JpaRepository<EventInfo, String> {

    @Query("SELECT e FROM EventInfo e " +
            "WHERE (:is_free = 'all' OR e.is_free = :is_free) " +
            "AND (:mcodename = '전체' OR :mcodename = 'null' OR e.mcodename = :mcodename) " +
            "AND (:codename = '전체' OR :codename = 'null' OR e.codename = :codename) " +
            "AND (:title IS NULL OR e.title LIKE CONCAT('%', :title, '%'))")
    Page<EventInfo> findFilteredEventInfo(
            @Param("is_free") String is_free,
            @Param("mcodename") String mcodename,
            @Param("codename") String codename,
            @Param("title") String title,
            Pageable pageable);
    EventInfo findByEventId(Long eventId);
}
