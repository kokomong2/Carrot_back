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
import org.springframework.web.multipart.MultipartFile;


import javax.transaction.Transactional;
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
    private final AwsS3Service awsS3Service;

    //게시물 작성
    @Transactional
    public ResponseEntity createPost(PostRequestDto requestDto, List<MultipartFile> files, UserDetailsImpl userDetails) {
        // file 업로드
        List<String> imageUrls = awsS3Service.uploadFile(files);

        Post post = Post.builder()
                .username(userDetails.getUsername())
                .profileImage(userDetails.getUser().getProfileImage())
                .nickname(userDetails.getUser().getNickname())
                .title(requestDto.getTitle())
                .content(requestDto.getContent().replace("\r\n", "<br>"))
                .price(requestDto.getPrice())
                .location(userDetails.getUser().getLocation())
                .build();
        postRepository.save(post);

        saveImageUrls(imageUrls, post);

        return new ResponseEntity(PostResponseDto.fromPost(post), HttpStatus.OK);
    }

    //모든 게시물 조회
    public List<PostResponseDto> getAllPosts() {
        List<Post> posts = postRepository.findAllByOrderByModifiedAtDesc();
        return makeList(posts);
    }

    //중복 검색 방지용 출처: https://howtodoinjava.com/java8/java-stream-distinct-examples/
    public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
        Map<Object, Boolean> map = new ConcurrentHashMap<>();
        return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    //키워드로 검색 (title, location, content)
    public List<PostResponseDto> searchPostsByKeyword(String keyword) {
        List<Post> postsByTitle = postRepository.findByTitleContaining(keyword);
        List<Post> postsByLocation = postRepository.findByLocationContaining(keyword);
        List<Post> postsByContent = postRepository.findByContentContaining(keyword);
        List<Post> postsByNickname = postRepository.findByNicknameContaining(keyword);
        List<PostResponseDto> postResponseDtos = new ArrayList<>();
        postResponseDtos.addAll(makeList(postsByTitle));
        postResponseDtos.addAll(makeList(postsByLocation));
        postResponseDtos.addAll(makeList(postsByContent));
        postResponseDtos.addAll(makeList(postsByNickname));
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

//    //닉네임으로 게시글 검색
//    public List<PostResponseDto> getPostsByNickname(String nickname) {
//        List<Post> posts = postRepository.findByNickname(nickname);
//        return makeList(posts);
//    }
//
//    //지역구로 게시글 검색
//    public List<PostResponseDto> getPostsByLocation(String location ) {
//        List<Post> posts = postRepository.findByLocation(location);
//        return makeList(posts);
//    }

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
    @Transactional
    public ResponseEntity update(Long id, String username, PostRequestDto requestDto) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException()
        );
        if (post.getUsername().equals(username)) {
            post.update(requestDto, id);
            postRepository.save(post);
            return new ResponseEntity(PostResponseDto.fromPost(post), HttpStatus.OK);
        } else {
            return new ResponseEntity("수정 불가능한 게시글입니다.", HttpStatus.UNAUTHORIZED); //401: 해당 요청에 대한 권한이 없는 상태
        }
    }

    //게시물 삭제
    public ResponseEntity delete(Long id, String username) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException()
        );
        if (post.getUsername().equals(username)) {
            deleteImageUrls(post);
            postRepository.delete(post);
            return new ResponseEntity("삭제 완료", HttpStatus.OK);
        } else {
            return new ResponseEntity("삭제 불가능한 게시글입니다.", HttpStatus.UNAUTHORIZED); //401: 해당 요청에 대한 권한이 없는 상태
        }
    }

    //게시물 삭제 시 S3의 이미지 파일도 삭제
    private void deleteImageUrls(Post post) {
        List<ImageUrl> imageUrlList = post.getImageUrls();
        if (imageUrlList != null) {
            imageUrlList.forEach(imageUrl-> {
                // image.url 에서 filename 뽑기
                String fileName = imageUrl.getImageUrl().split("/")[3];
                awsS3Service.deleteFile(fileName);});
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
