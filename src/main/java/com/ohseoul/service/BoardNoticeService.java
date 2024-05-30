package com.ohseoul.service;

import com.ohseoul.constant.Role;
import com.ohseoul.dto.BoardNoticeDTO;
import com.ohseoul.dto.BoardNoticeFormDTO;
import com.ohseoul.dto.BoardNoticeImgDTO;
import com.ohseoul.dto.NoticeSearchDTO;
import com.ohseoul.entity.BoardNotice;
import com.ohseoul.entity.BoardNoticeImg;
import com.ohseoul.entity.Member;
import com.ohseoul.repository.BoardNoticeImgRepository;
import com.ohseoul.repository.BoardNoticeRepository;
import com.ohseoul.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardNoticeService {

    private final BoardNoticeRepository boardNoticeRepository;
    private final MemberRepository memberRepository;
    private final BoardNoticeImgService boardNoticeImgService;
    private final BoardNoticeImgRepository boardNoticeImgRepository;



    @Transactional(readOnly = true)
    public Page<BoardNotice> getNoticeListPage(NoticeSearchDTO noticeSearchDTO, Pageable pageable){

        return boardNoticeRepository.getNoticeListPage(noticeSearchDTO,pageable);
    }


    public BoardNoticeFormDTO getNoticeMemberId(Long communityNo){

        List<BoardNoticeImg> boardCommunityImgList = boardNoticeImgRepository.findByBoardNoticeId(communityNo);
        List<BoardNoticeImgDTO> boardNoticeImgDTOList = new ArrayList<>();

        for (BoardNoticeImg boardNoticeImg : boardCommunityImgList){

            BoardNoticeImgDTO boardNoticeImgDTO = BoardNoticeImgDTO.of(boardNoticeImg);

            boardNoticeImgDTOList.add(boardNoticeImgDTO);
        }
        BoardNotice boardNotice =boardNoticeRepository.findById(communityNo).orElseThrow(EntityNotFoundException::new);

        BoardNoticeFormDTO boardNoticeFormDTO = BoardNoticeFormDTO.of(boardNotice);

        boardNoticeFormDTO.setNoticeNo(boardNotice.getId());

        boardNoticeFormDTO.setBoardNoticeImgDTO(boardNoticeImgDTOList);

        return boardNoticeFormDTO;
    }



    public Long saveNotice(BoardNoticeFormDTO boardNoticeFormDTO, List<MultipartFile> noticeImgFileList) throws Exception{
        BoardNotice boardNotice = boardNoticeFormDTO.createBoard();
        boardNoticeRepository.save(boardNotice);

        for (int i = 0; i<noticeImgFileList.size(); i++){
            BoardNoticeImg boardNoticeImg = new BoardNoticeImg();
            boardNoticeImg.setBoardNotice(boardNotice);
            boardNoticeImgService.saveNoticeImg(boardNoticeImg,noticeImgFileList.get(i));
        }
        return boardNotice.getId();
    }


    public Long updateNotice(BoardNoticeFormDTO boardNoticeFormDTO, List<MultipartFile> noticeImgFileList,String email) throws Exception{
        System.out.println("boardCommunityFormDTO++++++"+boardNoticeFormDTO);
        BoardNotice boardNotice = boardNoticeRepository.findById(boardNoticeFormDTO.getNoticeNo()).orElseThrow(EntityNotFoundException::new);
        System.out.println("boardCommunity 서비스 2222"+boardNoticeFormDTO);
        boardNotice.updateNoticeCommunity(boardNoticeFormDTO);
        System.out.println("boardCommunity 서비스 333"+boardNoticeFormDTO);
        List<Long> noticeImgIds = boardNoticeFormDTO.getNoticeImgIds();
        System.out.println("boardCommunity 서비스 444"+noticeImgIds);
        Member member= memberRepository.findByEmail(email);
        Role role = member.getRole();
        if(noticeImgIds.size() > 0){
            for(int i =0; i<noticeImgFileList.size(); i++){
                boardNoticeImgService.updateNoticeImg(noticeImgIds.get(i),noticeImgFileList.get(i));
            }
        }

        if(boardNotice !=null && boardNotice.getCreatedBy().equals(email) || role == Role.ADMIN){
            boardNoticeRepository.save(boardNotice);
            return boardNotice.getId();
        }
        else {
            throw new Exception("수정 권한이 없습니다.");
        }

    }

    public void deleteNotice(Long communityNo,String email) throws Exception {

        BoardNotice boardNotice = boardNoticeRepository.findById(communityNo).orElseThrow(EntityNotFoundException::new);
        Member member= memberRepository.findByEmail(email);
        Role role = member.getRole();

        if(boardNotice !=null && boardNotice.getCreatedBy().equals(email) || role == Role.ADMIN){
            boardNoticeRepository.deleteById(communityNo);
        }
        else {
            // 조회된 리뷰가 없거나 작성자와 일치하지 않을 경우
            // 업데이트할 리뷰가 존재하지 않는다는 것을 나타내기 위해 null 대신 다른 값(여기서는 15L)을 반환합니다.
            throw new Exception("리뷰를 삭제할 수 있는 권한이 없습니다.");
        }


    }


    public BoardNotice increaseViewCount(Long communityNo) {
        BoardNotice boardNotice = boardNoticeRepository.findById(communityNo)
                .orElseThrow(EntityNotFoundException::new);

        // 조회수를 1 증가시킵니다.
        boardNotice.setNoticeViewCount(boardNotice.getNoticeViewCount() + 1);

        // 증가된 조회수를 저장하고 업데이트된 BoardCommunity 객체를 반환합니다.
        return boardNoticeRepository.save(boardNotice);
    }




    public List<BoardNoticeDTO> myBoardCommunity(String email) {

        List<BoardNotice> boardNotices = boardNoticeRepository.findByCreatedBy(email);

        Member member = memberRepository.findByEmail(email);

        List<BoardNoticeDTO> boardNoticeDTOS = new ArrayList<>();

        for (BoardNotice boardNotice : boardNotices) {
            BoardNoticeDTO boardNoticeDTO = new BoardNoticeDTO();
            boardNoticeDTO.setNoticeTitle(boardNotice.getNoticeTitle());
            // 나머지 필드도 위와 같이 설정
            boardNoticeDTO.setId(boardNotice.getId());
//            boardCommunityDTO.setCommunityTitle();
            boardNoticeDTO.setRegTime(boardNotice.getRegTime());
            boardNoticeDTO.setNickName(member.getMemberNickname());
            boardNoticeDTOS.add(boardNoticeDTO);
        }

        return boardNoticeDTOS;
    }

}
