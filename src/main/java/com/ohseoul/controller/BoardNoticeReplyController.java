package com.ohseoul.controller;

import com.ohseoul.entity.BoardNoticeReply;
import com.ohseoul.repository.BoardNoticeReplyRepository;
import com.ohseoul.service.BoardNoticeReplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@Controller
@RequiredArgsConstructor

public class BoardNoticeReplyController {
    private final BoardNoticeReplyService boardNoticeReplyService;
    private final BoardNoticeReplyRepository boardNoticeReplyRepository;

    @PostMapping("/user/noticeReply/insert/{noticeId}")
    public String addNoticeReply(@PathVariable("noticeId") Long noticeId,
                           @RequestBody Map<String, String> requestData, Principal principal) {
        System.out.println("공지사항 댓글 입력 컨트롤러 입장 ");
        String reply = requestData.get("reply");
        System.out.println("공지사항 댓글 입력 컨트롤러 입장 reply==========="+reply);
        boardNoticeReplyService.insertReplyNotice(noticeId, principal.getName(), reply);
        System.out.println();
        // 댓글 등록 후 리다이렉트할 경로 또는 화면 반환
        return "redirect:/community/board/view/" + noticeId;
    }
    @PutMapping("/user/noticeReply/update/{replyId}")
    public ResponseEntity<Long> replyNoticeUpdate(@PathVariable Long replyId,
                                            @RequestBody Map<String, String> requestData, BindingResult bindingResult, Principal principal){
        Long replyno = Long.parseLong(requestData.get("replyId"));
        String reply = requestData.get("reply");
        String email = principal.getName();
        if(!bindingResult.hasErrors()){
            BoardNoticeReply boardNoticeReply = boardNoticeReplyRepository.findByNoticeReplyNo(replyno);
            boardNoticeReplyService.updateReplyNotice(replyno,email,reply);
            return ResponseEntity.ok().body(boardNoticeReply.getNoticeReplyNo());
        }

        return ResponseEntity.badRequest().body(null);

    }

    @DeleteMapping("/user/noticeReply/delete/{replyId}")
    public ResponseEntity<Long> replyNoticeDelete(@PathVariable Long replyId,@RequestBody Map<String, String> requestData, BindingResult bindingResult, Principal principal){

        Long replyno = Long.parseLong(requestData.get("replyId"));

        String email = principal.getName();
        if(!bindingResult.hasErrors()){
            BoardNoticeReply boardCommunityReply = boardNoticeReplyRepository.findByNoticeReplyNo(replyno);
            boardNoticeReplyService.deleteReplyNotice(replyno,email);
            return ResponseEntity.ok().body(boardCommunityReply.getNoticeReplyNo());
        }

        return ResponseEntity.badRequest().body(null);
    }
}
