package com.ohseoul.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class BoardCommunityDTO {
    private Long id;
    private String communityTitle;
    private String communityContent;
    private Long communityViewCount;
    private LocalDateTime regTime;
    private LocalDateTime updateTime;
    private String nickName;
}
