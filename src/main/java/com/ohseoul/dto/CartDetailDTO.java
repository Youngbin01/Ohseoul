package com.ohseoul.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.net.URL;

@Getter
@Setter
@ToString
public class CartDetailDTO {
    public CartDetailDTO(Long cartItemId, Long eventId, String title, String date, String place, URL main_img) {
        this.cartItemId = cartItemId;
        this.eventId = eventId;
        this.title = title;
        this.date = date;
        this.place = place;
        this.main_img = main_img;
    }

    private Long cartItemId;
    private Long eventId;
    private String title;
    private String date;
    private String place;
    private URL main_img;
}
