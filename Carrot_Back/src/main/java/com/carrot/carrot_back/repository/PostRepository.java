package com.carrot.carrot_back.repository;

import com.carrot.carrot_back.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post>findAllByLocation(String location);
    List<Post>findAllByOrderByModifiedAtDesc();
    List<Post> findByTitleContaining(String keyword);
    Optional<Post> findByPostingId(Long postingId); //왜 이건 항상 optional인가?
}
