package com.carrot.carrot_back.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String password;


}
