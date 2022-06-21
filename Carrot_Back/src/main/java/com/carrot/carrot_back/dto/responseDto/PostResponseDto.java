package com.carrot.carrot_back.dto.responseDto;

import com.carrot.carrot_back.model.ImageUrl;
import com.carrot.carrot_back.model.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;


@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostResponseDto {
    private Long id;
    private String username;
    private String profileImage;
    private String nickname;
    private String title;
    private String content;
    private int price;
    private String location;
    private List<ImageUrl> imageUrls;
    private String calculatedTime;


    //시간 x초 전, x분 전, x시간 전, x일 전, x달 전, x년 전 표시
    public static String calculatedTime(Post post) {
        final int SEC = 60;
        final int MIN = 60;
        final int HOUR = 24;
        final int DAY = 30;
        final int MONTH = 12;
        String msg = null;

        LocalDateTime rightNow = LocalDateTime.now();
        LocalDateTime createdAt = post.getCreatedAt();
        long MILLIS = ChronoUnit.MILLIS.between(createdAt, rightNow);
        long calculate = MILLIS/1000;

        if (calculate < SEC){
            msg = calculate + "초 전";
        } else if ((calculate /= SEC) < MIN) {
            msg = calculate + "분 전";
        } else if ((calculate /= MIN) < HOUR) {
            msg = (calculate) + "시간 전";
        } else if ((calculate /= HOUR) < DAY) {
            msg = (calculate) + "일 전";
        } else if ((calculate /= DAY) < MONTH) {
            msg = (calculate) + "개월 전";
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat( "yyyy");
            String curYear = sdf.format(rightNow);
            String passYear = sdf.format(createdAt);
            int diffYear = Integer.parseInt(curYear) - Integer.parseInt(passYear);
            msg = diffYear + "년 전";
        }
        return msg;
    }


    public static PostResponseDto fromPost(Post post) {
        return new PostResponseDto(
                post.getId(),
                post.getUsername(),
                post.getProfileImage(),
                post.getNickname(),
                post.getTitle(),
                post.getContent(),
                post.getPrice(),
                post.getLocation(),
                post.getImageUrls(),
                calculatedTime(post)
        );
    }

    @Setter
    @Getter
    @AllArgsConstructor
    public static class UserResponseDto {
        private String username;
        private String nickname;

    }
}
