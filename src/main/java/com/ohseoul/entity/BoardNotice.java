package com.ohseoul.entity;

import com.ohseoul.dto.BoardNoticeFormDTO;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class BoardNotice extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name= "notice_no")
    private Long id;
    private String noticeTitle;
    private String noticeContent;
    @Column(name = "notice_view_count")
    private Long noticeViewCount = 0L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_no")
    Member member;


    public void updateNoticeCommunity(BoardNoticeFormDTO boardnoticeFormDTO){
        this.id = boardnoticeFormDTO.getNoticeNo();
        this.noticeTitle = boardnoticeFormDTO.getNoticeTitle();
        this.noticeContent = boardnoticeFormDTO.getNoticeContent();
    }

    @OneToMany(mappedBy = "boardNotice", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BoardNoticeImg> noticeImgs = new ArrayList<>();

    @OneToMany(mappedBy = "boardNotice", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BoardNoticeReply> noticeReplies = new ArrayList<>();
}
