package com.carrot.carrot_back.model;

import com.carrot.carrot_back.dto.requestDto.PostRequestDto;
import com.carrot.carrot_back.security.UserDetailsImpl;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

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

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ImageUrl> imageUrls;


    @Builder
    public Post(String nickname, String title, String content, int price, String location) {
        this.nickname = nickname;
        this.title = title;
        this.content = content;
        this.price = price;
        this.location = location;
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
