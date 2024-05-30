package com.ohseoul.service;

import com.ohseoul.constant.Role;
import com.ohseoul.dto.BoardCommunityDTO;
import com.ohseoul.dto.BoardCommunityFormDTO;
import com.ohseoul.dto.BoardCommunityImgDTO;
import com.ohseoul.dto.BoardSearchDTO;
import com.ohseoul.entity.BoardCommunity;
import com.ohseoul.entity.BoardCommunityImg;
import com.ohseoul.entity.Member;
import com.ohseoul.repository.BoardCommunityImgRepository;
import com.ohseoul.repository.BoardCommunityRepository;
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
@Transactional
public class BoardCommunityService {

    private final BoardCommunityRepository boardCommunityRepository;
    private final MemberRepository memberRepository;
    private final BoardCommunityImgService boardCommunityImgService;
    private final BoardCommunityImgRepository boardCommunityImgRepository;

    @Transactional(readOnly = true)
    public Page<BoardCommunity> getBoardListPage(BoardSearchDTO boardSearchDTO, Pageable pageable){

        return boardCommunityRepository.getBoardListPage(boardSearchDTO,pageable);
    }


    public BoardCommunityFormDTO getMemberId(Long communityNo){

        List<BoardCommunityImg> boardCommunityImgList = boardCommunityImgRepository.findByBoardCommunityId(communityNo);
        List<BoardCommunityImgDTO> boardCommunityImgDTOList = new ArrayList<>();
        for (BoardCommunityImg boardCommunityImg : boardCommunityImgList){

            BoardCommunityImgDTO boardCommunityImgDTO = BoardCommunityImgDTO.of(boardCommunityImg);

            boardCommunityImgDTOList.add(boardCommunityImgDTO);
        }
        BoardCommunity boardCommunity =boardCommunityRepository.findById(communityNo).orElseThrow(EntityNotFoundException::new);

        BoardCommunityFormDTO boardCommunityFormDTO = BoardCommunityFormDTO.of(boardCommunity);

        boardCommunityFormDTO.setCommunityNo(boardCommunity.getId());

        boardCommunityFormDTO.setBoardCommunityImgDTOList(boardCommunityImgDTOList);

        return boardCommunityFormDTO;
    }



    public Long saveBoard(BoardCommunityFormDTO boardCommunityFormDTO, List<MultipartFile> boardImgFileList) throws Exception{
        BoardCommunity boardCommunity = boardCommunityFormDTO.createBoard();
        boardCommunityRepository.save(boardCommunity);

        for (int i = 0; i<boardImgFileList.size(); i++){
            BoardCommunityImg boardCommunityImg = new BoardCommunityImg();
            boardCommunityImg.setBoardCommunity(boardCommunity);
            boardCommunityImgService.saveCommunityImg(boardCommunityImg,boardImgFileList.get(i));
        }
        return boardCommunity.getId();
    }


    public Long updateCommunity(BoardCommunityFormDTO boardCommunityFormDTO, List<MultipartFile> boardImgFileList,String email) throws Exception{
        System.out.println("boardCommunityFormDTO++++++"+boardCommunityFormDTO);
        BoardCommunity boardCommunity = boardCommunityRepository.findById(boardCommunityFormDTO.getCommunityNo()).orElseThrow(EntityNotFoundException::new);
        System.out.println("boardCommunity 서비스 2222"+boardCommunityFormDTO);
        boardCommunity.updateCommunity(boardCommunityFormDTO);
        System.out.println("boardCommunity 서비스 333"+boardCommunityFormDTO);
        List<Long> boardImgIds = boardCommunityFormDTO.getBoardImgIds();
        System.out.println("boardCommunity 서비스 444"+boardImgIds);
        Member member= memberRepository.findByEmail(email);
        Role role = member.getRole();
        if(boardImgIds.size() > 0){
            for(int i =0; i<boardImgFileList.size(); i++){
                boardCommunityImgService.updateBoardImg(boardImgIds.get(i),boardImgFileList.get(i));
            }
        }

        if(boardCommunity !=null && boardCommunity.getCreatedBy().equals(email) || role == Role.ADMIN){
            boardCommunityRepository.save(boardCommunity);
            return boardCommunity.getId();
        }
        else {
            throw new Exception("수정 권한이 없습니다.");
        }

    }

    public void deleteCommunity(Long communityNo,String email) throws Exception {

        BoardCommunity boardCommunity = boardCommunityRepository.findById(communityNo).orElseThrow(EntityNotFoundException::new);
        Member member= memberRepository.findByEmail(email);
        Role role = member.getRole();

        if(boardCommunity !=null && boardCommunity.getCreatedBy().equals(email) || role == Role.ADMIN){
            boardCommunityRepository.deleteById(communityNo);
        }
        else {
            // 조회된 리뷰가 없거나 작성자와 일치하지 않을 경우
            // 업데이트할 리뷰가 존재하지 않는다는 것을 나타내기 위해 null 대신 다른 값(여기서는 15L)을 반환합니다.
            throw new Exception("리뷰를 삭제할 수 있는 권한이 없습니다.");
        }


    }


    public BoardCommunity increaseViewCount(Long communityNo) {
        BoardCommunity boardCommunity = boardCommunityRepository.findById(communityNo)
                .orElseThrow(EntityNotFoundException::new);

        // 조회수를 1 증가시킵니다.
        boardCommunity.setCommunityViewCount(boardCommunity.getCommunityViewCount() + 1);

        // 증가된 조회수를 저장하고 업데이트된 BoardCommunity 객체를 반환합니다.
        return boardCommunityRepository.save(boardCommunity);
    }




    public List<BoardCommunityDTO> myBoardCommunity(String email) {
        List<BoardCommunity> boardCommunities = boardCommunityRepository.findByCreatedBy(email);

        Member member = memberRepository.findByEmail(email);
        List<BoardCommunityDTO> boardCommunityDTOs = new ArrayList<>();

        for (BoardCommunity boardCommunity : boardCommunities) {
            BoardCommunityDTO boardCommunityDTO = new BoardCommunityDTO();
            boardCommunityDTO.setCommunityTitle(boardCommunity.getCommunityTitle());
            // 나머지 필드도 위와 같이 설정
            boardCommunityDTO.setId(boardCommunity.getId());
//            boardCommunityDTO.setCommunityTitle();
            boardCommunityDTO.setRegTime(boardCommunity.getRegTime());
            boardCommunityDTO.setNickName(member.getMemberNickname());
            boardCommunityDTOs.add(boardCommunityDTO);
        }

        return boardCommunityDTOs;
    }




}
