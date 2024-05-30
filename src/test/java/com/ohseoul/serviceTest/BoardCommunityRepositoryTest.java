package com.ohseoul.serviceTest;

import com.ohseoul.entity.BoardCommunity;
import com.ohseoul.entity.QBoardCommunity;
import com.ohseoul.repository.BoardCommunityRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@SpringBootTest
@RequiredArgsConstructor
public class BoardCommunityRepositoryTest {
    @Autowired
     BoardCommunityRepository boardCommunityRepository;


    @PersistenceContext
    EntityManager em;


    @Test
    public void DslTest(){
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QBoardCommunity qBoardCommunity = QBoardCommunity.boardCommunity;

        List<BoardCommunity> list =  queryFactory.select(qBoardCommunity)
                .from(qBoardCommunity)
                .where(qBoardCommunity.communityTitle.like("%"+"1"+"%"))
                .fetch();
        for(BoardCommunity boardCommunity : list){
            System.out.println("boardCommunity = "+boardCommunity);
        }

    }

}
