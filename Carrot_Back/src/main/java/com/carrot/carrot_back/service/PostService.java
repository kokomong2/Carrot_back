package com.carrot.carrot_back.service;

import com.carrot.carrot_back.dto.requestDto.PostRequestDto;
import com.carrot.carrot_back.dto.responseDto.PostResponseDto;
import com.carrot.carrot_back.model.Post;
import com.carrot.carrot_back.model.User;
import com.carrot.carrot_back.repository.PostRepository;
import com.carrot.carrot_back.security.UserDetailsImpl;
import com.sun.xml.internal.ws.developer.Serialization;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    //게시물 작성
    public ResponseEntity createPost(PostRequestDto requestDto, UserDetailsImpl userDetails) {
        Post post = Post.builder()
                .nickname(userDetails.getUser().getNickname())
                .title(requestDto.getTitle())
                .content(requestDto.getContent().replace("\r\n","<br>"))
                .price(requestDto.getPrice())
                .location(requestDto.getLocation())
                .imageUrl(requestDto.getImageUrl())
                .build();
        postRepository.save(post);
        return new ResponseEntity("등록 성공", HttpStatus.OK);
    }


    //지역별로 게시물 조회
    public List<PostResponseDto> getPostsByLocation(String location) {
        List<Post> posts = postRepository.findAllByLocation(location);
        List<PostResponseDto> postResponseDtos = new ArrayList<>();
        for (Post post : posts) {
            PostResponseDto postResponseDto = new PostResponseDto(
                    post.getPostingId(),
                    post.getNickname(),
                    post.getTitle(),
                    post.getContent(),
                    post.getPrice(),
                    post.getLocation(),
                    post.getImageUrl(),
                    post.getCreatedAt()
            );
            postResponseDtos.add(postResponseDto);
        }
        return postResponseDtos;
    }

    //모든 게시물 조회
    public List<PostResponseDto> getAllPosts() {
        List<Post> posts = postRepository.findAllByOrderByModifiedAtDesc();
        List<PostResponseDto> postResponseDtos = new ArrayList<>();
        for (Post post : posts) {
            PostResponseDto postResponseDto = new PostResponseDto(
                    post.getPostingId(),
                    post.getNickname(),
                    post.getTitle(),
                    post.getContent(),
                    post.getPrice(),
                    post.getLocation(),
                    post.getImageUrl(),
                    post.getCreatedAt()
            );
            postResponseDtos.add(postResponseDto);
        }
        return postResponseDtos;
    }

    //검색어로 게시물 조회(title에 해당하는 keyword로 검색)
    public List<PostResponseDto> getSearchedPosts(String keyword) {
        List<Post> posts = postRepository.findByTitleContaining(keyword);
        List<PostResponseDto> postResponseDtos = new ArrayList<>();
        for (Post post : posts) {
            PostResponseDto postResponseDto = new PostResponseDto(
                    post.getPostingId(),
                    post.getNickname(),
                    post.getTitle(),
                    post.getContent(),
                    post.getPrice(),
                    post.getLocation(),
                    post.getImageUrl(),
                    post.getCreatedAt()
            );
            postResponseDtos.add(postResponseDto);
        }
        return postResponseDtos;
    }

    //해당 게시물 상세페이지 (게시물 1개 조회)
    public PostResponseDto getPost(Long postingId) {
        Post post = postRepository.findByPostingId(postingId).orElseThrow(
                () -> new IllegalArgumentException()
        );
        return new PostResponseDto(
                post.getPostingId(),
                post.getNickname(),
                post.getTitle(),
                post.getContent(),
                post.getPrice(),
                post.getLocation(),
                post.getImageUrl(),
                post.getCreatedAt()
        );
    }

    //게시물 수정
    public ResponseEntity update(Long postingId, String nickname, PostRequestDto requestDto, UserDetailsImpl userDetails) {
        Post post = postRepository.findByPostingId(postingId).orElseThrow(
                () -> new IllegalArgumentException()
        );
        if (post.getNickname().equals(nickname)) {
            post.update(requestDto, postingId, userDetails);
            postRepository.save(post);
            return new ResponseEntity("수정 완료", HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.valueOf(403));
        }
    }


    public ResponseEntity delete(Long postingId, String nickname) {
        Post post = postRepository.findByPostingId(postingId).orElseThrow(
                () -> new IllegalArgumentException()
        );
        if (post.getNickname().equals(nickname)) {
            postRepository.delete(post);
            return new ResponseEntity("삭제 완료", HttpStatus.OK);
        } else {
            return new ResponseEntity("삭제 불가능한 게시글입니다.", HttpStatus.valueOf(403));
        }
    }


}
