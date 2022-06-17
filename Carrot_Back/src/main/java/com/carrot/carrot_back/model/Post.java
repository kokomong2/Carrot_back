package com.carrot.carrot_back.model;

import com.carrot.carrot_back.dto.requestDto.PostRequestDto;
import com.carrot.carrot_back.security.UserDetailsImpl;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.mapping.List;
import org.w3c.dom.stylesheets.LinkStyle;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
public class Post extends Timestamped{
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long postingId;

    @Column
    private String nickname;

    @Column
    private String title;

    @Column
    private String content;

    @Column
    private int price;

    @Column
    private String location;

    @Column
    private String imageUrl;

    @Builder
    public Post(String nickname, String title, String content, int price, String location, String imageUrl) {
        this.nickname = nickname;
        this.title = title;
        this.content = content;
        this.price = price;
        this.location = location;
        this.imageUrl = imageUrl;
    }

    public void update(PostRequestDto requestDto, Long postingId, UserDetailsImpl userDetails) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.price = Integer.parseInt(requestDto.getPrice());
        this.location = requestDto.getLocation();
        this.postingId = postingId;
        this.nickname = userDetails.getUser().getNickname();
    }
}
