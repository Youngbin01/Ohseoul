package com.ohseoul.dto;

import com.ohseoul.entity.BoardCommunityImg;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.modelmapper.ModelMapper;

@Getter
@Setter
@ToString
public class BoardCommunityImgDTO {
    private Long communityNo;
    private Long id;
    private String imgName;
    private String oriImgName;
    private String imgUrl;

    private static ModelMapper modelMapper = new ModelMapper();

    public static BoardCommunityImgDTO of(BoardCommunityImg boardCommunityImg){
        return modelMapper.map(boardCommunityImg, BoardCommunityImgDTO.class);
    }
}
