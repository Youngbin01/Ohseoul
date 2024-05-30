package com.ohseoul.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class BoardCommunityReply extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long communityReplyNo;
    private String communityReplyContent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="community_no")
    private BoardCommunity boardCommunity;



}

