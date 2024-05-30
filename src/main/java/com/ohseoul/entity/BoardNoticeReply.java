package com.ohseoul.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class BoardNoticeReply extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long noticeReplyNo;
    private String noticeReplyContent;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="notice_no" )
    private BoardNotice boardNotice;

}
