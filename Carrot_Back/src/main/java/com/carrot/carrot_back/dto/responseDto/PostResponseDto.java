package com.carrot.carrot_back.dto.responseDto;

import com.carrot.carrot_back.dto.requestDto.PostRequestDto;
import com.carrot.carrot_back.model.ImageUrl;
import com.carrot.carrot_back.model.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostResponseDto {
    private Long id;
    private String username;
    private String nickname;
    private String title;
    private String content;
    private int price;
    private String location;
    private LocalDateTime createdAt;
    private List<ImageUrl> imageUrls;

    public static PostResponseDto fromPost(Post post) {
        return new PostResponseDto(
                post.getId(),
                post.getUsername(),
                post.getNickname(),
                post.getTitle(),
                post.getContent(),
                post.getPrice(),
                post.getLocation(),
                post.getCreatedAt(),
                post.getImageUrls()
        );
    }
}
