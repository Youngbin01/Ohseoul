package com.ohseoul.entity;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@Table(name="cart_item")
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
    private EventInfo eventInfo;



    public static CartItem createCartItem(Cart cart, EventInfo eventInfo){
        CartItem cartItem=new CartItem();
        cartItem.setCart(cart);
        cartItem.setEventInfo(eventInfo);
        return cartItem;
    }



}
