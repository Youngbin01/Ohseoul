package com.ohseoul.repository;

import com.ohseoul.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart findByMemberMemberNo(Long memberNo);

}