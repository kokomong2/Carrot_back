package com.carrot.carrot_back.dto.responseDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostResponseDto {
    private Long postingId;
    private String nickname;
    private String title;
    private String content;
    private int price;
    private String location;
    private String imageUrl;
    private LocalDateTime createdAt;
}
