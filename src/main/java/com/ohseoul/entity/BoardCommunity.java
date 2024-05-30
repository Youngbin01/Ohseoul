package com.ohseoul.entity;

import com.ohseoul.dto.BoardCommunityFormDTO;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class BoardCommunity extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "community_no")
    private Long id;
    private String communityTitle;
    private String communityContent;
    @Column(name = "community_view_count")
    private Long communityViewCount = 0L; // 디폴트 값 설

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_no")
    Member member;

    public void updateCommunity(BoardCommunityFormDTO boardCommunityFormDTO){
        this.id = boardCommunityFormDTO.getCommunityNo();
        this.communityTitle = boardCommunityFormDTO.getCommunityTitle();
        this.communityContent = boardCommunityFormDTO.getCommunityContent();
    }
    @OneToMany(mappedBy = "boardCommunity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BoardCommunityImg> communityImgs = new ArrayList<>();

    @OneToMany(mappedBy = "boardCommunity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BoardCommunityReply> communityReplies = new ArrayList<>();

}
