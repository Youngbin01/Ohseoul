package com.ohseoul.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@ToString
@Getter
@Setter
public class MemberFormDTO {

    @NotNull
    private String email;
    @NotNull
    private String password;
    @NotNull
    private String nickName;
}