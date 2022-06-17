package com.carrot.carrot_back.service;

import com.carrot.carrot_back.dto.requestDto.PostRequestDto;
import com.carrot.carrot_back.dto.responseDto.PostResponseDto;
import com.carrot.carrot_back.model.ImageUrl;
import com.carrot.carrot_back.model.Post;
import com.carrot.carrot_back.repository.ImageUrlRepository;
import com.carrot.carrot_back.repository.PostRepository;
import com.carrot.carrot_back.security.UserDetailsImpl;
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
    private final ImageUrlRepository imageUrlRepository;

    //게시물 작성
    public ResponseEntity createPost(PostRequestDto requestDto, UserDetailsImpl userDetails) {
        Post post = Post.builder()
                .username(userDetails.getUsername())
                .nickname(userDetails.getUser().getNickname())
                .title(requestDto.getTitle())
                .content(requestDto.getContent().replace("\r\n", "<br>"))
                .price(requestDto.getPrice())
                .location(requestDto.getLocation())
                .build();
        postRepository.save(post);

        saveImageUrls(requestDto.getImageUrls(), post);

        return new ResponseEntity("등록 성공", HttpStatus.OK);
    }


    //지역별로 게시물 조회
    public List<PostResponseDto> getPostsByLocation(String location) {
        List<Post> posts = postRepository.findAllByLocation(location);
        List<PostResponseDto> postResponseDtos = new ArrayList<>();
        for (Post post : posts) {
            PostResponseDto postResponseDto = PostResponseDto.fromPost(post);
            postResponseDtos.add(postResponseDto);
        }
        return postResponseDtos;
    }

    //모든 게시물 조회
    public List<PostResponseDto> getAllPosts() {
        List<Post> posts = postRepository.findAllByOrderByModifiedAtDesc();
        List<PostResponseDto> postResponseDtos = new ArrayList<>();
        for (Post post : posts) {
            PostResponseDto postResponseDto = PostResponseDto.fromPost(post);
            postResponseDtos.add(postResponseDto);
        }
        return postResponseDtos;
    }

    //검색어로 게시물 조회(title에 해당하는 keyword로 검색)
    public List<PostResponseDto> getSearchedPosts(String keyword) {
        List<Post> posts = postRepository.findByTitleContaining(keyword);
        List<PostResponseDto> postResponseDtos = new ArrayList<>();
        for (Post post : posts) {
            PostResponseDto postResponseDto = PostResponseDto.fromPost(post);
            postResponseDtos.add(postResponseDto);
        }
        return postResponseDtos;
    }

    //내가 쓴 게시물 조회
    public List<PostResponseDto> getMyPosts(String username) {
        List<Post> posts = postRepository.findAllByUsername(username);
        List<PostResponseDto> postResponseDtos = new ArrayList<>();
        for (Post myPost : posts) {
            PostResponseDto postResponseDto = PostResponseDto.fromPost(myPost);
            postResponseDtos.add(postResponseDto);
        }
            return postResponseDtos;
    }

    public List<PostResponseDto> getPostsViaNickname(String nickname) {
        List<Post> posts = postRepository.findAllByNickname(nickname);
        List<PostResponseDto> postResponseDtos = new ArrayList<>();
        for (Post post : posts) {
            PostResponseDto postResponseDto = PostResponseDto.fromPost(post);
            postResponseDtos.add(postResponseDto);
        }
        return postResponseDtos;
    }

    //해당 게시물 상세페이지 (게시물 1개 조회)
    public PostResponseDto getPost(Long id) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException()
        );
        return PostResponseDto.fromPost(post);
    }

    //게시물 수정
    public ResponseEntity update(Long id, String nickname, PostRequestDto requestDto) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException()
        );
        if (post.getNickname().equals(nickname)) {
            post.update(requestDto, id);
            postRepository.save(post);
            return new ResponseEntity("수정 완료", HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.valueOf(403));
        }
    }

    //게시물 삭제
    public ResponseEntity delete(Long id, String nickname) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException()
        );
        if (post.getNickname().equals(nickname)) {
            postRepository.delete(post);
            return new ResponseEntity("삭제 완료", HttpStatus.OK);
        } else {
            return new ResponseEntity("삭제 불가능한 게시글입니다.", HttpStatus.valueOf(403));
        }
    }

    private List<ImageUrl> saveImageUrls(List<String> imageUrls, Post post) {
        List<ImageUrl> imageUrlList = new ArrayList<>();

        imageUrls.forEach(imageUrl -> {
            imageUrlList.add(ImageUrl.builder()
                    .imageUrl(imageUrl)
                    .post(post)
                    .build());
        });
        return imageUrlRepository.saveAll(imageUrlList);
    }
}
