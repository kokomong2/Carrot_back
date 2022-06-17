package com.carrot.carrot_back.dto.requestDto;

import lombok.Getter;

import java.util.List;

@Getter
public class PostRequestDto {
    private String title;
    private String content;
    private String price;
    private String location;
    private String imageUrl;

}
