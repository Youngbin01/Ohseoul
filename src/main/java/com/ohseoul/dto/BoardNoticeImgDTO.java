package com.ohseoul.dto;

import com.ohseoul.entity.BoardNoticeImg;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.modelmapper.ModelMapper;

@Getter
@Setter
@ToString
public class BoardNoticeImgDTO {

    private Long noticeNo;
    private Long id;
    private String imgName;
    private String oriImgName;
    private String imgUrl;

    private static ModelMapper modelMapper = new ModelMapper();

    public static BoardNoticeImgDTO of(BoardNoticeImg boardNoticeImg){
        return modelMapper.map(boardNoticeImg, BoardNoticeImgDTO.class);
    }
}
