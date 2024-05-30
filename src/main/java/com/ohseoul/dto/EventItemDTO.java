package com.ohseoul.dto;

import lombok.Getter;
import lombok.Setter;

import java.net.URL;

@Getter
@Setter
public class EventItemDTO {
    private Long  eventId;
    private String title;
    private String date;
    private URL main_img;
    private double lot;
    private double lat;
    private String use_trgt;
    private String use_fee;
    private String mcodename;
    private String codename;
    private URL hmpg_addr;
    private String place;

    // 생성자, 게터, 세터 생략

    // 필요한 경우 생성자, 게터, 세터를 추가할 수 있습니다.
}
