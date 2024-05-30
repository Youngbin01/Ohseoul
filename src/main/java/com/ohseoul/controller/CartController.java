package com.ohseoul.controller;

import com.ohseoul.dto.CartDetailDTO;
import com.ohseoul.dto.CartItemDTO;
import com.ohseoul.entity.Member;
import com.ohseoul.repository.MemberRepository;
import com.ohseoul.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

import static com.ohseoul.entity.QMember.member;

@Controller
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;
    private final MemberRepository memberRepository;


    @PostMapping("/cart")
    public @ResponseBody ResponseEntity order(@RequestBody @Valid CartItemDTO cartItemDTO, BindingResult bindingResult, Principal principal){
        if (bindingResult.hasErrors()){
            StringBuilder sb=new StringBuilder();
            List<FieldError>fieldErrorList=bindingResult.getFieldErrors();
            for (FieldError fieldError:fieldErrorList){
                sb.append(fieldError.getDefaultMessage());
            }
            return new ResponseEntity(sb.toString(), HttpStatus.BAD_REQUEST);
        }
        String email= principal.getName();
        Long cartItemId;

        try{
            cartItemId=cartService.addCart(cartItemDTO, email);
        }catch (Exception e){
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(cartItemId, HttpStatus.OK);
    }


    @GetMapping("/cart")
    public String orderHist(Principal principal, Model model){
        List<CartDetailDTO> cartDetailDTOList=cartService.getCartList(principal.getName());
        String email= principal.getName();
        Member member = memberRepository.findByEmail(email);
        model.addAttribute("social", member.isSocial());
        model.addAttribute("cartItems", cartDetailDTOList);
        return "mypage/cart-list";
    }

    @DeleteMapping("/cartItem/{cartItemId}")
    public @ResponseBody ResponseEntity deleteCartItem(
            @PathVariable("cartItemId")Long cartItemId, Principal principal){
        if (!cartService.validationCartItem(cartItemId, principal.getName())){
            return new ResponseEntity("수정권한이 없습니다.", HttpStatus.FORBIDDEN);
        }
        cartService.deleteCartItem(cartItemId);
        return new ResponseEntity(cartItemId, HttpStatus.OK);
    }

    @DeleteMapping("/cart")
    public @ResponseBody ResponseEntity deleteAllCartItems(Principal principal) {
        try {
            cartService.deleteAllCartItems(principal.getName());
            return new ResponseEntity<>("장바구니 상품을 모두 삭제했습니다.", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
