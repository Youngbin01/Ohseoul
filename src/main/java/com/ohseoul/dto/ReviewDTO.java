package com.ohseoul.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class ReviewDTO {
    private Long ReviewNo;
    private String createdBy;
    private String email;
    private String review;
    private LocalDateTime regTime;
    private LocalDateTime updateTime;


}
