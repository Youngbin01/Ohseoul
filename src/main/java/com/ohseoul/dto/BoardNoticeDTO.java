package com.ohseoul.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class BoardNoticeDTO {
    private Long id;
    private String noticeTitle;
    private String noticeContent;
    private Long noticeViewCount;
    private LocalDateTime regTime;
    private LocalDateTime updateTime;
    private String nickName;
}
