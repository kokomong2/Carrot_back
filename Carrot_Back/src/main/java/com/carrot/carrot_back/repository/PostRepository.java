package com.carrot.carrot_back.repository;

import com.carrot.carrot_back.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post>findAllByOrderByModifiedAtDesc();
    List<Post> findByTitleContaining(String keyword);
    List<Post> findByLocationContaining(String keyword);
    List<Post> findByContentContaining(String keyword);
    List<Post> findByNicknameContaining(String keyword);
    Optional<Post> findById(Long id); //왜 이건 항상 optional인가?
    List<Post> findByUsername(String username);
    List<Post> findByNickname(String nickname);
    List<Post>findByLocation(String location);
}
