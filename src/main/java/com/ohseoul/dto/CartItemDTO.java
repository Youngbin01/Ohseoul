package com.ohseoul.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class CartItemDTO {
    @NotNull(message = "상품 아이디는 필수 입력입니다.")
    private Long itemId;
}
