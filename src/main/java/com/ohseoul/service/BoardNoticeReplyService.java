package com.ohseoul.service;

import com.ohseoul.constant.Role;
import com.ohseoul.dto.ReplyDTO;
import com.ohseoul.entity.BoardNotice;
import com.ohseoul.entity.BoardNoticeReply;
import com.ohseoul.entity.Member;
import com.ohseoul.repository.BoardNoticeReplyRepository;
import com.ohseoul.repository.BoardNoticeRepository;
import com.ohseoul.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardNoticeReplyService {
    private final BoardNoticeReplyRepository boardNoticeReplyRepository;
    private final MemberRepository memberRepository;
    private final BoardNoticeRepository boardNoticeRepository;

    public Long insertReplyNotice(Long communityId, String email, String reply) {
        Member member = memberRepository.findByEmail(email);
        if (member == null) {
            throw new UsernameNotFoundException("유저를 찾을수가 없습니다. 로그인후 이용해 주세요");
        }
        BoardNotice boardNotice = boardNoticeRepository.findById(communityId).orElseThrow();
        System.out.println("insert 서비스 코드 ++++"+boardNotice);
        System.out.println("insert 서비스 코드 ++++"+boardNotice);

        if(boardNotice == null) {
            throw new RuntimeException("해당 게시글을 찾을수 없습니다");
        }
        BoardNoticeReply boardNoticeReply = new BoardNoticeReply();
        boardNoticeReply.setNoticeReplyContent(reply);
        boardNoticeReply.setBoardNotice(boardNotice);
        System.out.println("insert reply 서비스 코드   "+boardNoticeReply);
        boardNoticeReplyRepository.save(boardNoticeReply);
        return boardNoticeReply.getNoticeReplyNo();


    }


    public List<ReplyDTO> getNoticeReplyList(Long noticeId) {
        List<BoardNoticeReply> boardNoticeReplies = boardNoticeReplyRepository.findByBoardNoticeId(noticeId);
        if(boardNoticeReplies != null && !boardNoticeReplies.isEmpty()) {
            List<ReplyDTO> replyDTOList = new ArrayList<>();
            for (BoardNoticeReply boardNoticeReply : boardNoticeReplies) {
                ReplyDTO replyDTO = new ReplyDTO();
                Member member = memberRepository.findByEmail(boardNoticeReply.getCreatedBy());
                replyDTO.setEmail(boardNoticeReply.getCreatedBy());
                replyDTO.setCreatedBy(member.getMemberNickname());
                replyDTO.setReplyNo(boardNoticeReply.getNoticeReplyNo());
                replyDTO.setReply(boardNoticeReply.getNoticeReplyContent());
//                replyDTO.setCreatedBy(boardCommunityReply.getCreatedBy());
                replyDTO.setRegTime(boardNoticeReply.getRegTime());
                replyDTO.setUpdateTime(boardNoticeReply.getUpdateTime());

                replyDTOList.add(replyDTO);
            }
            return replyDTOList;
        }else {
            return null;
        }
    }
    public Long updateReplyNotice(Long replyNo, String email, String reply) {
        System.out.println("reviewNo++++++" + replyNo);
        // 주어진 리뷰 번호에 해당하는 리뷰를 데이터베이스에서 조회합니다.
        Optional<BoardNoticeReply> optionalBoardNoticeReply = Optional.ofNullable(boardNoticeReplyRepository.findByNoticeReplyNo(replyNo));
        BoardNoticeReply boardNoticeReply = optionalBoardNoticeReply.orElseThrow(() -> new ReplyUpdateException("해당 리뷰를 찾을 수 없습니다."));
        Member member = memberRepository.findByEmail(email);
        Role Role = member.getRole();
        System.out.println("Role++++++ 서비스" + Role);
        // 조회된 리뷰가 존재하고, 작성자가 주어진 이메일과 일치하는지 확인합니다.
        if (boardNoticeReply != null && boardNoticeReply.getCreatedBy().equals(email) || Role == Role.ADMIN ) {
            // 조회된 리뷰가 존재하고 작성자와 일치할 경우 해당 리뷰의 내용을 업데이트합니다.
            boardNoticeReply.setNoticeReplyContent(reply);
            // 변경된 리뷰를 저장합니다.
            boardNoticeReplyRepository.save(boardNoticeReply);
            // 업데이트된 리뷰의 리뷰 번호를 반환합니다.
            return boardNoticeReply.getNoticeReplyNo();
        }

        else {
            // 조회된 리뷰가 없거나 작성자와 일치하지 않을 경우
            // 업데이트할 리뷰가 존재하지 않는다는 것을 나타내기 위해 null 대신 다른 값(여기서는 15L)을 반환합니다.
            throw new ReplyUpdateException("리뷰를 업데이트할 수 있는 권한이 없습니다.");
        }
    }


    public Long deleteReplyNotice(Long replyNo, String email) {
        System.out.println("reviewNo++++++" + replyNo);
        // 주어진 리뷰 번호에 해당하는 리뷰를 데이터베이스에서 조회합니다.
        Optional<BoardNoticeReply> optionalBoardNoticeReply = Optional.ofNullable(boardNoticeReplyRepository.findByNoticeReplyNo(replyNo));
        BoardNoticeReply boardNoticeReply = optionalBoardNoticeReply.orElseThrow(() -> new ReplyUpdateException("해당 리뷰를 찾을 수 없습니다."));
        System.out.println("boardReview++++++" + boardNoticeReply);
        Member member = memberRepository.findByEmail(email);
        Role Role = member.getRole();
        // 조회된 리뷰가 존재하고, 작성자가 주어진 이메일과 일치하는지 확인합니다. 관리자일때도 가능
        if (boardNoticeReply != null && boardNoticeReply.getCreatedBy().equals(email) || Role == Role.ADMIN) {
            // 조회된 리뷰가 존재하고 작성자와 일치할 경우 해당 리뷰의 내용을 업데이트합니다.
            boardNoticeReply.setNoticeReplyNo(replyNo);
            // 변경된 리뷰를 저장합니다.
            boardNoticeReplyRepository.delete(boardNoticeReply);
            // 업데이트된 리뷰의 리뷰 번호를 반환합니다.
            return boardNoticeReply.getNoticeReplyNo();
        } else {
            // 조회된 리뷰가 없거나 작성자와 일치하지 않을 경우
            // 업데이트할 리뷰가 존재하지 않는다는 것을 나타내기 위해 null 대신 다른 값(여기서는 15L)을 반환합니다.
            throw new ReplyUpdateException("리뷰를 삭제할 수 있는 권한이 없습니다.");
        }
    }





    public class ReplyUpdateException extends RuntimeException {
        public ReplyUpdateException(String message) {
            super(message);
        }
    }






}
