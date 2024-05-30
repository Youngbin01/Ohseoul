package com.ohseoul.repository;

import com.ohseoul.dto.BoardSearchDTO;
import com.ohseoul.entity.BoardCommunity;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import java.util.List;

import static com.ohseoul.entity.QBoardCommunity.boardCommunity;


public class BoardCommunityRepositoryCustomImpl implements BoardCommunityRepositoryCustom {

    private JPAQueryFactory queryFactory;

    public BoardCommunityRepositoryCustomImpl(EntityManager em){
        this.queryFactory =new JPAQueryFactory(em);
    }

    public BooleanExpression searchByLike(String searchCreatedBy, String searchQuery,String searchCommunityContent, String searchName){

//        String searchCreatedBy, String searchQuery,String searchCommunityContent, String searchName

        System.out.println("searchQuery test"+searchQuery);
        if(StringUtils.equals("createdBy",searchCreatedBy)){
            System.out.println("BoardCommunityRepositoryCustomImp테스트  createdBy"+searchQuery);
            return boardCommunity.createdBy.like("%"+searchQuery+"%");
        } else if (StringUtils.equals("communityContent",searchCommunityContent)) {
            System.out.println("BoardCommunityRepositoryCustomImp테스트  communityContent"+searchQuery);
            return boardCommunity.communityContent.like("%"+searchQuery+"%");
        } else if (StringUtils.equals("searchName",searchName)) {
            System.out.println("BoardCommunityRepositoryCustomImp테스트 searchName"+searchQuery);
            return boardCommunity.communityTitle.like("%"+searchQuery+"%");
        }
        return null;
    }
    @Override
    public Page<BoardCommunity> getBoardListPage(BoardSearchDTO boardSearchDTO, Pageable pageable) {
//        System.out.println("BoardSearchDTO===========>"+boardSearchDTO);
//        System.out.println("pageable==================>"+pageable);
        System.out.println("getBoardListPage===========boardSearchDTO=======>"+boardSearchDTO);

        List<BoardCommunity> result = queryFactory.selectFrom(boardCommunity)
                .where(searchByLike(boardSearchDTO.getSearchCreatedBy(),
                        boardSearchDTO.getSearchQuery(),
                        boardSearchDTO.getSearchCommunityContent(),
                        boardSearchDTO.getSearchName()))
                                                   .orderBy(boardCommunity.id.desc())
                                                   .offset(pageable.getOffset())
                                                   .limit(pageable.getPageSize())
                                                   .fetch();

        Long total = queryFactory.select(Wildcard.count)
                                 .from(boardCommunity)
                                 .where(searchByLike(boardSearchDTO.getSearchCreatedBy(),
                                                     boardSearchDTO.getSearchQuery(),
                                                     boardSearchDTO.getSearchCommunityContent(),
                                                     boardSearchDTO.getSearchName()))
                                 .fetchOne();
//        String searchCreatedBy, String searchQuery,String searchCommunityContent, String searchName
        return new PageImpl<>(result, pageable, total);
    }
}
