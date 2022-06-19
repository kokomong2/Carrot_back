package com.carrot.carrot_back.service;

import com.carrot.carrot_back.dto.requestDto.PostRequestDto;
import com.carrot.carrot_back.dto.responseDto.PostResponseDto;
import com.carrot.carrot_back.model.ImageUrl;
import com.carrot.carrot_back.model.Post;
import com.carrot.carrot_back.repository.ImageUrlRepository;
import com.carrot.carrot_back.repository.PostRepository;
import com.carrot.carrot_back.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.cglib.core.internal.Function;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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
                .location(userDetails.getUser().getLocation())
                .build();
        postRepository.save(post);

        saveImageUrls(requestDto.getImageUrls(), post);

        return new ResponseEntity(post, HttpStatus.OK);
    }


//    //지역별로 게시물 조회
//    public List<PostResponseDto> getPostsByLocation(String location) {
//        List<Post> posts = postRepository.findAllByLocation(location);
//        List<PostResponseDto> postResponseDtos = new ArrayList<>();
//        makeList(posts);
//        return postResponseDtos;
//    }

    //모든 게시물 조회
    public List<PostResponseDto> getAllPosts() {
        List<Post> posts = postRepository.findAllByOrderByModifiedAtDesc();
        return makeList(posts);
    }

    //중복 검색 방지용 https://howtodoinjava.com/java8/java-stream-distinct-examples/
    public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
        Map<Object, Boolean> map = new ConcurrentHashMap<>();
        return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    //키워드로 검색 (title, location, content)
    public List<PostResponseDto> searchPostsByKeyword(String keyword) {
        List<Post> postsByTitle = postRepository.findByTitleContaining(keyword);
        List<Post> postsByLocation = postRepository.findByLocationContaining(keyword);
        List<Post> postsByContent = postRepository.findByContentContaining(keyword);
        List<PostResponseDto> postResponseDtos = new ArrayList<>();
        postResponseDtos.addAll(makeList(postsByTitle));
        postResponseDtos.addAll(makeList(postsByLocation));
        postResponseDtos.addAll(makeList(postsByContent));
//        if (postResponseDtos.isEmpty()) {
//            return getAllPosts();
//        }
        return postResponseDtos.stream()
                .filter(distinctByKey(p -> p.getId())) //중복되는 게시글 id값으로 비교해서 걸러주기.
                .collect(Collectors.toList());
    }

    //내가 쓴 게시물 조회
    public List<PostResponseDto> getMyPosts(String username) {
        List<Post> posts = postRepository.findByUsername(username);
        if (posts != null) {
            return makeList(posts);
        }
        throw new IllegalArgumentException("작성한 게시글이 없습니다.");
    }

    //닉네임으로 게시글 검색
    public List<PostResponseDto> getPostsByNickname(String nickname) {
        List<Post> posts = postRepository.findByNickname(nickname);
        return makeList(posts);
    }

    //for문 돌리는 함수
    public List<PostResponseDto> makeList(List<Post> postList) {
        List<PostResponseDto> postResponseDtos = new ArrayList<>();
        for (Post post : postList) {
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
    public ResponseEntity update(Long id, String username, PostRequestDto requestDto) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException()
        );
        if (post.getUsername().equals(username)) {
            post.update(requestDto, id);
            postRepository.save(post);
            return new ResponseEntity(post, HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.valueOf(403));
        }
    }

    //게시물 삭제
    public ResponseEntity delete(Long id, String username) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException()
        );
        if (post.getUsername().equals(username)) {
            postRepository.delete(post);
            return new ResponseEntity("삭제 완료", HttpStatus.OK);
        } else {
            return new ResponseEntity("삭제 불가능한 게시글입니다.", HttpStatus.valueOf(403));
        }
    }

    //imageUrl들을 imageUrls리스트로 만들어주기
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
