package com.carrot.carrot_back.dto;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;

@Setter
@Getter
public class SignupRequestDto {
    private String username;
    private String nickname;
    private String password;
    private String profileImage;
    private String location;
//    private String checkPassword;
}