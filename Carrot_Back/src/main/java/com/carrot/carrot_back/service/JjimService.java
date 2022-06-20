package com.carrot.carrot_back.service;

import com.carrot.carrot_back.dto.JjimDto;
import com.carrot.carrot_back.dto.JjimResponseDto;
import com.carrot.carrot_back.model.Jjim;
import com.carrot.carrot_back.model.Post;
import com.carrot.carrot_back.model.User;
import com.carrot.carrot_back.repository.JjimRepository;
import com.carrot.carrot_back.repository.PostRepository;
import com.carrot.carrot_back.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class JjimService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final JjimRepository jjimRepository;

    // 찜 누르기,취소
    public boolean jjimUp(Long postingId, String username) {
        Post post = getPost(postingId);
        User user = getUser(username);



        boolean toggleLike;

        JjimDto jjimDto = new JjimDto(post, user);
        Jjim jjim = new Jjim(jjimDto);
        int jjimCnt = jjim.getPost().getJjimCnt();

        Jjim findJjim = jjimRepository.findByPostAndUser(post, user).orElse(null);

        if(findJjim == null){
            jjim.getPost().setJjimCnt(jjimCnt+1);

            jjimRepository.save(jjim);
            toggleLike = true;
        }
        else{
            jjim.getPost().setJjimCnt(jjimCnt-1);

            jjimRepository.deleteById(findJjim.getJjimId());
            toggleLike = false;
        }
        return toggleLike;
    }
    private Post getPost(Long postingId) {
        Post post = postRepository.findById(postingId).orElseThrow(
                ()->new IllegalArgumentException("게시글이 존재하지 않습니다.")
        );
        return post;
    }

    private User getUser(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("사용자 정보가 존재하지 않습니다.")
        );
        return user;
    }


//    public List<JjimResponseDto> getJjim(Long postingId) {
//        List<JjimResponseDto> jjimResponseDtos = new ArrayList<>();
//
//    }
}

