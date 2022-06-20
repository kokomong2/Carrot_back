package com.carrot.carrot_back.dto;


import com.carrot.carrot_back.model.Post;
import com.carrot.carrot_back.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JjimDto {
    private Post post;
    private User user;
}
