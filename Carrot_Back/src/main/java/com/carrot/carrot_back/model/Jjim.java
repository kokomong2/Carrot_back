package com.carrot.carrot_back.model;

import com.carrot.carrot_back.dto.JjimDto;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Jjim {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long jjimId;

    public Jjim(JjimDto jjimDto) {
        this.post = jjimDto.getPost();
        this.user = jjimDto.getUser();
    }

    @ManyToOne
    @JoinColumn(name = "postingId")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "username")
    private User user;

}
