package com.carrot.carrot_back.model;

import com.carrot.carrot_back.dto.requestDto.PostRequestDto;
import com.carrot.carrot_back.security.UserDetailsImpl;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@Getter
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Post extends Timestamped{
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column
    private String username;

    @Column
    private String profileImage;

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

    @JsonIgnore
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ImageUrl> imageUrls;


    @Builder
    public Post(String username, String profileImage,  String nickname, String title, String content, int price, String location, List<ImageUrl> imageUrls) {
        this.username = username;
        this.profileImage = profileImage;
        this.nickname = nickname;
        this.title = title;
        this.content = content;
        this.price = price;
        this.location = location;
        this.imageUrls = imageUrls;
    }

    public Post(PostRequestDto requestDto, UserDetailsImpl userDetails) {
        this.username = userDetails.getUsername();
        this.profileImage = userDetails.getUser().getProfileImage();
        this.nickname = userDetails.getUser().getNickname();
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.price = requestDto.getPrice();
        this.location = userDetails.getUser().getLocation();
        this.imageUrls = getImageUrls();
    }

    public void update(PostRequestDto requestDto, Long id) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.price = requestDto.getPrice();
        this.id = id;
    }

}
