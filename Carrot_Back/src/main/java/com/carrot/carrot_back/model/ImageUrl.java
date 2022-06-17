package com.carrot.carrot_back.model;

import com.carrot.carrot_back.dto.requestDto.PostRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.awt.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Builder
public class ImageUrl {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name= "post_id")
    private Post post;

}
