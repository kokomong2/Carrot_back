package com.carrot.carrot_back.repository;

import com.carrot.carrot_back.model.Jjim;
import com.carrot.carrot_back.model.Post;
import com.carrot.carrot_back.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface JjimRepository extends JpaRepository<Jjim, Long> {

    Optional<Jjim> findByPostAndUser(Post post, User user);
}
