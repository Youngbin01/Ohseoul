package com.ohseoul.service;

import com.ohseoul.dto.CartDetailDTO;
import com.ohseoul.dto.CartItemDTO;
import com.ohseoul.entity.Cart;
import com.ohseoul.entity.CartItem;
import com.ohseoul.entity.EventInfo;
import com.ohseoul.entity.Member;
import com.ohseoul.repository.CartItemRepository;
import com.ohseoul.repository.CartRepository;
import com.ohseoul.repository.EventInfoRepository;
import com.ohseoul.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {
    private final MemberRepository memberRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final EventInfoRepository eventInfoRepository;

    public Long addCart(CartItemDTO cartItemDTO, String email) {

        //장바구니에 넣을 상품 조회
        EventInfo eventInfo = eventInfoRepository.findByEventId(cartItemDTO.getItemId());
        if (eventInfo == null) {
            throw new EntityNotFoundException("EventInfo not found for id: " + cartItemDTO.getItemId());
        }

        //로그인한 회원 엔티티 조회
        Member member = memberRepository.findByEmail(email);

        //현재 회원의 장바구니가 있는지 조회
        Cart cart = cartRepository.findByMemberMemberNo(member.getMemberNo());
        //장바구니가 없으면 생성, 있으면 수량을 증가시키거나 장바구니 상품을 추가하기
        if (cart == null) {
            cart = Cart.createCart(member);
            cartRepository.save(cart);
        }
        //장바구니에 상품이 들어 있는지 확인 후 있으면 수량 add, 없으면 장바구니 상품을 추가하기
        CartItem saveCartItem = cartItemRepository.findByCartIdAndEventInfoEventId(cart.getId(), eventInfo.getEventId());
        if (saveCartItem != null) {
            throw new IllegalStateException("이미 장바구니에 상품이 존재합니다");
        } else {
            CartItem cartItem = CartItem.createCartItem(cart, eventInfo);
            cartItemRepository.save(cartItem);
            return cartItem.getId();
        }


    }


    @Transactional(readOnly = true)
    public List<CartDetailDTO> getCartList(String email) {
        List<CartDetailDTO> cartDetailDTOList = new ArrayList<>();

        Member member = memberRepository.findByEmail(email);
        Cart cart = cartRepository.findByMemberMemberNo(member.getMemberNo());
        if (cart == null) {
            return cartDetailDTOList;
        }
        cartDetailDTOList = cartItemRepository.findCartDetailDTOList(cart.getId());
        return cartDetailDTOList;
    }


    //현재 로그인 한 회원과 장바구니에 상품을 저장한 회원을 체크하여 같으면 true 다르면  false
    @Transactional(readOnly = true)
    public boolean validationCartItem(Long cartItemId, String email){
        Member member=memberRepository.findByEmail(email);
        CartItem cartItem=cartItemRepository.findById(cartItemId).orElseThrow(EntityNotFoundException::new);
        Member savedMember=cartItem.getCart().getMember();

        if(!StringUtils.equals(member.getEmail(), savedMember.getEmail())){
            return false;
        }
        return true;
    }

    public void deleteCartItem(Long cartItemId) {
        CartItem cartItem=cartItemRepository.findById(cartItemId).orElseThrow(EntityNotFoundException::new);
        cartItemRepository.delete(cartItem);
    }


    public void deleteAllCartItems(String email) {
        List<CartItem> cartItems = cartItemRepository.findByEmail(email);
        cartItemRepository.deleteAll(cartItems);
    }

}
