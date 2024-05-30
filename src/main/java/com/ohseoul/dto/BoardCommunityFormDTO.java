package com.ohseoul.dto;

import com.ohseoul.entity.BoardCommunity;
import com.ohseoul.entity.Member;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.modelmapper.ModelMapper;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class BoardCommunityFormDTO {

    private Long communityNo;
    @NotBlank(message = "제목은 필수 입력입니다.")
    private String communityTitle;
    @NotBlank(message = "내용을 필수 입력입니다.")
    private String communityContent;
    private LocalDateTime regTime;
    private Long communityViewCount = 0L;
    private Member member;

    private List<BoardCommunityImgDTO> boardCommunityImgDTOList = new ArrayList<>();

    private List<Long> boardImgIds = new ArrayList<>();

    private static ModelMapper modelMapper = new ModelMapper();

    public BoardCommunity createBoard(){
        return modelMapper.map(this,BoardCommunity.class);
    }

    public static BoardCommunityFormDTO of(BoardCommunity boardCommunity){
        return modelMapper.map(boardCommunity,BoardCommunityFormDTO.class);
    }


}
