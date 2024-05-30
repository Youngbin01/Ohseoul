package com.ohseoul.dto;

import lombok.Getter;
import lombok.Setter;

import java.net.URL;

@Getter
@Setter
public class EventDTO {

    private Long  eventId;
    private String title;
    private String date;
    private URL main_img;
}
