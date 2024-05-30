package com.ohseoul.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor

public class Cart {
    @Id
    @Column(name = "cart_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //    @OneToOne: 회원테이블과 1:1 매핑
//    @JoinColumn: 외래키 지정, name = "member_id": 외래키의 이름
    //즉시 로딩 전략:카트를 읽을 때 member를 바로 읽어 옴.
    // @OneToOne과 @ManyToOne는 EAGER전략이 default임
    //@OneToMany, @ManyToMany는 LAZY전략(지연로딩){쓸 때 가지고옴, 필요할 때}
    //일대일 단방향 관계, foreign키가 있는 넘이 주인공 타입은 참조하는 넘 타입
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberNo")
    private Member member;

    public static Cart createCart(Member member){
        Cart cart=new Cart();
        cart.setMember(member);
        return cart;
    }
}
