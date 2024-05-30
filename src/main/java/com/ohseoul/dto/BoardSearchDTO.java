package com.ohseoul.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BoardSearchDTO {

    private String searchCreatedBy;
    private String searchCommunityContent;
    private String searchQuery;
    private String searchName;
    private String searchCriteria;

}
