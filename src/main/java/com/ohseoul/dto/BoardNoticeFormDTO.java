package com.ohseoul.dto;

import com.ohseoul.entity.BoardNotice;
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
public class BoardNoticeFormDTO {

        private Long noticeNo;
        @NotBlank(message = "제목은 필수 입력입니다.")
        private String noticeTitle;
        @NotBlank(message = "내용을 필수 입력입니다.")
        private String noticeContent;
        private LocalDateTime regTime;
        private Long noticeViewCount = 0L;
        private Member member;

        private List<BoardNoticeImgDTO> boardNoticeImgDTO = new ArrayList<>();

        private List<Long> noticeImgIds = new ArrayList<>();

        private static ModelMapper modelMapper = new ModelMapper();

        public BoardNotice createBoard(){
            return modelMapper.map(this,BoardNotice.class);
        }

        public static BoardNoticeFormDTO of(BoardNotice boardNotice){
            return modelMapper.map(boardNotice, BoardNoticeFormDTO.class);
        }


    }


