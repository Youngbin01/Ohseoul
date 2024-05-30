package com.ohseoul.repository;

import com.ohseoul.dto.CartDetailDTO;
import com.ohseoul.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    CartItem findByCartIdAndEventInfoEventId(Long cartId, Long eventId);
    @Query("select new com.ohseoul.dto.CartDetailDTO(ci.id, i.eventId, i.title, i.date, i.place, i.main_img) " +
            "from CartItem ci " +
            "join ci.eventInfo i " +
            "where ci.cart.id = :cartId ")
    List<CartDetailDTO> findCartDetailDTOList(@Param("cartId") Long cartId);

    // 사용자의 이메일로 모든 장바구니 항목을 찾는 메서드
    @Query("select ci from CartItem ci where ci.cart.member.email = :email")
    List<CartItem> findByEmail(@Param("email") String email);
}
