package com.ohseoul.service;

import com.ohseoul.constant.Role;
import com.ohseoul.dto.ReviewDTO;
import com.ohseoul.entity.BoardReview;
import com.ohseoul.entity.EventInfo;
import com.ohseoul.entity.Member;
import com.ohseoul.repository.BoardReviewRepository;
import com.ohseoul.repository.EventInfoRepository;
import com.ohseoul.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardReviewService {
    private final BoardReviewRepository boardReviewRepository;
    private final MemberRepository memberRepository;
    private final EventInfoRepository eventInfoRepository;


    public Long insertReview(String review, Long eventId, String email) {
        // 이메일로 멤버를 찾는다.
        Member member = memberRepository.findByEmail(email);
        if (member == null) {
            throw new UsernameNotFoundException("해당하는 이메일(" + email + ")을 찾을 수 없습니다.");
        }
        // 이벤트 ID로 이벤트 정보를 찾는다.
        EventInfo eventInfo = eventInfoRepository.findByEventId(eventId);
        if (eventInfo == null) {
            throw new IllegalArgumentException("해당하는 게시물을 찾을 수 없습니다.");
        }
        // 리뷰 생성 및 저장
        BoardReview result = new BoardReview();
        result.setReviewContent(review);
        result.setEventInfo(eventInfo);
        boardReviewRepository.save(result);
        return result.getReviewNo();
    }


    public List<ReviewDTO> getEventReview(Long eventId) {
        System.out.println("eventId++++++" + eventId);
        EventInfo eventInfo = eventInfoRepository.findByEventId(eventId);
        System.out.println("eventId++++++" + eventInfo);
//        Member member = memberRepository.findByEmail()
//        System.out.println("eventId++++++"+boardReviewRepository.findByEventInfo(eventId));
        List<BoardReview> boardReviews = boardReviewRepository.findByEventInfoEventId(eventId);
        System.out.println("eventId++++++" + eventId);
        if (boardReviews != null && !boardReviews.isEmpty()) {
            List<ReviewDTO> reviewDTOList = new ArrayList<>();
            for (BoardReview boardReview : boardReviews) {
                ReviewDTO reviewDTO = new ReviewDTO();
                Member member = memberRepository.findByEmail(boardReview.getCreatedBy());
                reviewDTO.setEmail(boardReview.getCreatedBy());
                reviewDTO.setCreatedBy(member.getMemberNickname());
                reviewDTO.setReview(boardReview.getReviewContent());
                reviewDTO.setUpdateTime(boardReview.getUpdateTime());
                reviewDTO.setRegTime(boardReview.getRegTime());
                reviewDTO.setReviewNo(boardReview.getReviewNo());
                reviewDTOList.add(reviewDTO);
            }

            return reviewDTOList;
        } else {
            // 해당 타이틀의 공연 정보가 없을 경우 처리
            return null;
        }
    }


    public Long updateReview(Long reviewNo, String email, String review) {
        System.out.println("reviewNo++++++" + reviewNo);
        // 주어진 리뷰 번호에 해당하는 리뷰를 데이터베이스에서 조회합니다.
        Optional<BoardReview> optionalBoardReview = Optional.ofNullable(boardReviewRepository.findByReviewNo(reviewNo));
        BoardReview boardReview = optionalBoardReview.orElseThrow(() -> new ReviewUpdateException("해당 리뷰를 찾을 수 없습니다."));
        System.out.println("boardReview++++++" + boardReview);
        Member member = memberRepository.findByEmail(email);
        Role Role = member.getRole();
        System.out.println("Role++++++ 서비스" + Role);
        // 조회된 리뷰가 존재하고, 작성자가 주어진 이메일과 일치하는지 확인합니다.
        if (boardReview != null && boardReview.getCreatedBy().equals(email) || Role == Role.ADMIN ) {
            // 조회된 리뷰가 존재하고 작성자와 일치할 경우 해당 리뷰의 내용을 업데이트합니다.
            boardReview.setReviewContent(review);
            // 변경된 리뷰를 저장합니다.
            boardReviewRepository.save(boardReview);
            // 업데이트된 리뷰의 리뷰 번호를 반환합니다.
            return boardReview.getReviewNo();
        }

        else {
            // 조회된 리뷰가 없거나 작성자와 일치하지 않을 경우
            // 업데이트할 리뷰가 존재하지 않는다는 것을 나타내기 위해 null 대신 다른 값(여기서는 15L)을 반환합니다.
            throw new ReviewUpdateException("리뷰를 업데이트할 수 있는 권한이 없습니다.");
        }
    }


    public Long deleteReview(Long reviewNo, String email) {
        System.out.println("reviewNo++++++" + reviewNo);
        // 주어진 리뷰 번호에 해당하는 리뷰를 데이터베이스에서 조회합니다.
        Optional<BoardReview> optionalBoardReview = Optional.ofNullable(boardReviewRepository.findByReviewNo(reviewNo));
        BoardReview boardReview = optionalBoardReview.orElseThrow(() -> new ReviewUpdateException("해당 리뷰를 찾을 수 없습니다."));
        System.out.println("boardReview++++++" + boardReview);
        Member member = memberRepository.findByEmail(email);
        Role Role = member.getRole();
        // 조회된 리뷰가 존재하고, 작성자가 주어진 이메일과 일치하는지 확인합니다. 관리자일때도 가능
        if (boardReview != null && boardReview.getCreatedBy().equals(email) || Role == Role.ADMIN) {
            // 조회된 리뷰가 존재하고 작성자와 일치할 경우 해당 리뷰의 내용을 업데이트합니다.
            boardReview.setReviewNo(reviewNo);
            // 변경된 리뷰를 저장합니다.
            boardReviewRepository.delete(boardReview);
            // 업데이트된 리뷰의 리뷰 번호를 반환합니다.
            return boardReview.getReviewNo();
        } else {
            // 조회된 리뷰가 없거나 작성자와 일치하지 않을 경우
            // 업데이트할 리뷰가 존재하지 않는다는 것을 나타내기 위해 null 대신 다른 값(여기서는 15L)을 반환합니다.
            throw new ReviewUpdateException("리뷰를 삭제할 수 있는 권한이 없습니다.");
        }
    }














    public class ReviewUpdateException extends RuntimeException {
        public ReviewUpdateException(String message) {
            super(message);
        }
    }


}






