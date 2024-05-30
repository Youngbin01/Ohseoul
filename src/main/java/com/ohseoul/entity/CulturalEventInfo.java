package com.ohseoul.entity;

import lombok.Getter;
import lombok.ToString;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;


@Getter
@ToString
@XmlRootElement(name = "culturalEventInfo")
public class CulturalEventInfo {
    @XmlElement(name = "row")
    private List<EventInfo> eventInfoList;
}
