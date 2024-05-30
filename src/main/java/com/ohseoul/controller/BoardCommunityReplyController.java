package com.ohseoul.controller;

import com.ohseoul.entity.BoardCommunityReply;
import com.ohseoul.repository.BoardCommunityReplyRepository;
import com.ohseoul.service.BoardCommunityReplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@Controller
@RequiredArgsConstructor

public class BoardCommunityReplyController {

    private final BoardCommunityReplyService boardCommunityReplyService;
    private final BoardCommunityReplyRepository boardCommunityReplyRepository;

    @PostMapping("/user/reply/insert/{boardId}")
    public String addReply(@PathVariable("boardId") Long boardId,
                           @RequestBody Map<String, String> requestData, Principal principal) {
        String reply = requestData.get("reply");

        boardCommunityReplyService.insertReply(boardId, principal.getName(), reply);
        // 댓글 등록 후 리다이렉트할 경로 또는 화면 반환
        return "redirect:/community/board/view/" + boardId;
    }
    @PutMapping("/user/reply/update/{replyId}")
    public ResponseEntity<Long> replyUpdate(@PathVariable Long replyId,
                                             @RequestBody Map<String, String> requestData, BindingResult bindingResult, Principal principal){
        Long replyno = Long.parseLong(requestData.get("replyId"));
        String reply = requestData.get("reply");
        String email = principal.getName();
        if(!bindingResult.hasErrors()){
            BoardCommunityReply boardCommunityReply = boardCommunityReplyRepository.findByCommunityReplyNo(replyno);
            boardCommunityReplyService.updateReply(replyno,email,reply);
            return ResponseEntity.ok().body(boardCommunityReply.getCommunityReplyNo());
        }

        return ResponseEntity.badRequest().body(null);

    }

    @DeleteMapping("/user/reply/delete/{replyId}")
    public ResponseEntity<Long> replyDelete(@PathVariable Long replyId,@RequestBody Map<String, String> requestData, BindingResult bindingResult, Principal principal){

        Long replyno = Long.parseLong(requestData.get("replyId"));

        String email = principal.getName();
        if(!bindingResult.hasErrors()){
            BoardCommunityReply boardCommunityReply = boardCommunityReplyRepository.findByCommunityReplyNo(replyno);
            boardCommunityReplyService.deleteReply(replyno,email);
            return ResponseEntity.ok().body(boardCommunityReply.getCommunityReplyNo());
        }

        return ResponseEntity.badRequest().body(null);
    }
}
