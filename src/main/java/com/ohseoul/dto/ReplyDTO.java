package com.ohseoul.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class ReplyDTO {
    private Long ReplyNo;
    private String createdBy;
    private String reply;
    private String email;
    private LocalDateTime regTime;
    private LocalDateTime updateTime;
}
