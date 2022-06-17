package com.carrot.carrot_back.controller;

import com.carrot.carrot_back.dto.requestDto.PostRequestDto;
import com.carrot.carrot_back.dto.responseDto.PostResponseDto;
import com.carrot.carrot_back.model.Post;
import com.carrot.carrot_back.security.UserDetailsImpl;
import com.carrot.carrot_back.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    //게시글 등록
    @PostMapping("/api/posting")
    public ResponseEntity createPost(@RequestBody PostRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok().body(postService.createPost(requestDto, userDetails));
    }

    // location으로 찾기 및 전체 조회
    @GetMapping("/api/posts")
    public ResponseEntity<List<PostResponseDto>> readPosts(@RequestParam(required = false) String location) {
        if (location != null) {
            return ResponseEntity.ok().body(postService.getPostsByLocation(location));
        } else {
            return ResponseEntity.ok().body(postService.getAllPosts());
        }
    }

    //검색어로 찾기 (merge 후 확인 필요)
    @GetMapping("/api/posts/search")
    public ResponseEntity<List<PostResponseDto>> searchPosts(@RequestParam(required = false) String keyword) {
        if (keyword != null) {
            return ResponseEntity.ok().body(postService.getSearchedPosts(keyword));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //내가 작성한 게시글 조회
    @GetMapping("/api/posts/my")
    public ResponseEntity<List<PostResponseDto>> myPosts(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok().body(postService.getMyPosts(userDetails.getUsername()));
    }

    //닉네임으로 게시글 조회
    @GetMapping("/api/posts/{nickname}")
    public ResponseEntity getPost(@PathVariable String nickname) {
        return ResponseEntity.ok().body(postService.getPostsViaNickname(nickname));
    }

    //해당 게시물 상세 페이지
    @GetMapping("/api/post/{id}")
    public ResponseEntity getPost(@PathVariable Long id) {
        return ResponseEntity.ok().body(postService.getPost(id));
    }

    //게시글 수정
    @PutMapping("/api/post/{id}")
    public ResponseEntity undatePost(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody PostRequestDto requestDto) {
        return ResponseEntity.ok().body(postService.update(id, userDetails.getUser().getNickname(), requestDto));
    }

    //게시글 삭제
    @DeleteMapping("/api/post/{id}")
    public ResponseEntity deletePost(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok().body(postService.delete(id, userDetails.getUser().getNickname()));
    }
}
