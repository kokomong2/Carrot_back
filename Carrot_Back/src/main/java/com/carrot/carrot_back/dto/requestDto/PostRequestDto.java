package com.carrot.carrot_back.dto.requestDto;

import com.carrot.carrot_back.model.ImageUrl;
import lombok.Getter;

import java.util.List;

@Getter
public class PostRequestDto {
    private String title;
    private String content;
    private String price;
    private String location;
    private List<String> imageUrls;
}
