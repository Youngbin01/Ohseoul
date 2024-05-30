package com.ohseoul.repository;

import com.ohseoul.dto.NoticeSearchDTO;
import com.ohseoul.entity.BoardNotice;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import java.util.List;

import static com.ohseoul.entity.QBoardNotice.boardNotice;

public class BoardNoticeRepositoryCustomImpl implements BoardNoticeRepositoryCustom {

    private JPAQueryFactory queryFactory;

    public BoardNoticeRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    public BooleanExpression searchByLike(String searchCreatedBy, String searchQuery, String searchCommunityContent, String searchName){

//        String searchCreatedBy, String searchQuery,String searchCommunityContent, String searchName


        if(StringUtils.equals("createdBy",searchCreatedBy)){

            return boardNotice.createdBy.like("%"+searchQuery+"%");
        } else if (StringUtils.equals("communityContent",searchCommunityContent)) {

            return boardNotice.noticeContent.like("%"+searchQuery+"%");
        } else if (StringUtils.equals("searchName",searchName)) {

            return boardNotice.noticeTitle.like("%"+searchQuery+"%");
        }
        return null;
    }



    @Override
    public Page<BoardNotice> getNoticeListPage(NoticeSearchDTO noticeSearchDTO, Pageable pageable) {
//        System.out.println("BoardSearchDTO===========>"+boardSearchDTO);
//        System.out.println("pageable==================>"+pageable);


        List<BoardNotice> result = queryFactory.selectFrom(boardNotice)
                .where(searchByLike(noticeSearchDTO.getSearchCreatedBy(),
                        noticeSearchDTO.getSearchQuery(),
                        noticeSearchDTO.getSearchCommunityContent(),
                        noticeSearchDTO.getSearchName()))
                .orderBy(boardNotice.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory.select(Wildcard.count)
                .from(boardNotice)
                .where(searchByLike(noticeSearchDTO.getSearchCreatedBy(),
                        noticeSearchDTO.getSearchQuery(),
                        noticeSearchDTO.getSearchCommunityContent(),
                        noticeSearchDTO.getSearchName()))
                .fetchOne();
//        String searchCreatedBy, String searchQuery,String searchCommunityContent, String searchName
        return new PageImpl<>(result, pageable, total);
    }
}
