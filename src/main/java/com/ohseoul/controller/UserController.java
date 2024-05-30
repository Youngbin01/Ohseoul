package com.ohseoul.controller;

import com.ohseoul.dto.ReviewDTO;
import com.ohseoul.entity.BoardReview;
import com.ohseoul.repository.BoardReviewRepository;
import com.ohseoul.repository.MemberRepository;
import com.ohseoul.service.BoardReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final MemberRepository memberRepository;
    private final BoardReviewService reviewService;
    private final BoardReviewRepository reviewRepository;

    @PostMapping("/user/review/insert/{eventId}")
    public String reviewWrite(@PathVariable Long eventId
            , @RequestBody Map<String, String> requestData, Principal principal){

        String review = requestData.get("review");
        System.out.println("review테스트"+review);
        reviewService.insertReview(review,eventId,principal.getName());
        return "redirect:/schedule/" + eventId;
    }

    @PutMapping("/user/review/update/{reviewId}")
    public ResponseEntity<Long> reviewUpdate(@PathVariable Long reviewId,
                                             @RequestBody Map<String, String> requestData, BindingResult bindingResult, Principal principal){
        System.out.println("컨트롤러 들옴..!!");
        Long reviewno = Long.parseLong(requestData.get("reviewId"));
        String review = requestData.get("review");
        String email = principal.getName();
        if(!bindingResult.hasErrors()){
            BoardReview boardReview = reviewRepository.findByReviewNo(reviewno);
            reviewService.updateReview(reviewno,email,review);
            return ResponseEntity.ok().body(boardReview.getReviewNo());
        }

        return ResponseEntity.badRequest().body(null);

    }

    @DeleteMapping("/user/review/delete/{reviewId}")
    public ResponseEntity<Long> reviewDelete(@PathVariable Long reviewId,@RequestBody Map<String, String> requestData, BindingResult bindingResult, Principal principal){
        Long reviewno = Long.parseLong(requestData.get("reviewId"));
        String email = principal.getName();
        if(!bindingResult.hasErrors()){
            BoardReview boardReview = reviewRepository.findByReviewNo(reviewno);
            reviewService.deleteReview(reviewno,email);
            return ResponseEntity.ok().body(boardReview.getReviewNo());
        }

        return ResponseEntity.badRequest().body(null);
    }


}
