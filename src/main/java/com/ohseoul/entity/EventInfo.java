package com.ohseoul.entity;

import lombok.Getter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlElement;
import java.net.URL;


@Entity
@Getter

@ToString
public class EventInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @XmlElement(name = "event_id")
    private Long eventId;

    @XmlElement(name = "CODENAME")
    private String codename;

    @XmlElement(name = "GUNAME")
    private String guname;

    @XmlElement(name = "TITLE")
    private String title;

    @XmlElement(name = "DATE")
    private String date;

    @XmlElement(name = "PLACE")
    private String place;

    @XmlElement(name="MCODENAME")
    private String mcodename;

    @XmlElement(name="MAIN_IMG")
    private URL main_img;

    @XmlElement(name="IS_FREE")
    private String is_free;

    @XmlElement(name="USE_TRGT")
    private String use_trgt;

    @XmlElement(name="USE_FEE")
    private String use_fee;

    @XmlElement(name="LOT")
    private double lot;

    @XmlElement(name="LAT")
    private double lat;

    @XmlElement(name="HMPG_ADDR")
    private URL hmpg_addr;





    // 디폴트 생성자 추가
    public EventInfo() {
    }

    //setter 사용하지 못해 생성자를 통해 필드 초기화
    public EventInfo(String codename, String guname, String title, String date, String place, URL main_img , String is_free , String use_trgt, String use_fee, double lot, double lat, URL hmpg_addr) {
        this.codename = codename;
        this.guname = guname;
        this.title = title;
        this.date = date;
        this.place = place;
        this.main_img=main_img;
        this.is_free=is_free;
        this.use_trgt=use_trgt;
        this.use_fee=use_fee;
        this.lot=lot;
        this.lat=lat;
        this.hmpg_addr=hmpg_addr;



        // mcodename 값을 설정합니다.
        if ("연극".equals(codename) || "뮤지컬/오페라".equals(codename) || "콘서트".equals(codename)) {
            this.mcodename = "공연";
        } else if ("클래식".equals(codename) || "국악".equals(codename) || "무용".equals(codename) || "독주/독창회".equals(codename)|| "전시/미술".equals(codename)) {
            this.mcodename = "교양";
        } else if ("축제".equals(codename)) {
            this.mcodename = "축제";
        } else {
            this.mcodename = "기타";
        }
    }
}