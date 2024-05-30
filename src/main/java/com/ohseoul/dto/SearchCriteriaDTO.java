package com.ohseoul.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchCriteriaDTO {
    private String is_free; // 무료/유료 구분을 위한 필드
    private String mcodename; // 공연, 교양, 축제, 기타
    private String codename; // 세부 항목
    private String searchKeyword; // 검색어
}
