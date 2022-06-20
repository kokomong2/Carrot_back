package com.carrot.carrot_back.controller;

import com.carrot.carrot_back.dto.JjimResponseDto;
import com.carrot.carrot_back.model.Jjim;
import com.carrot.carrot_back.security.UserDetailsImpl;
import com.carrot.carrot_back.service.JjimService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor // 생성자를 자동으로 생성해주는 어노테이션이다. final 키워드가 붙은 멤버변수를 생성자 파라미터로 알아서 넣어준다.
@RestController //@RestController는 단순히 객체만을 반환하고 객체 데이터는 JSON 또는 XML 형식으로 HTTP 응답에 담아서 전송합니다
public class JjimController {
    private final JjimService jjimService;
    // 찜 누르기,취소
    @PostMapping("/api/posts/{posting}/jjim")
    public ResponseEntity<Boolean> clickJjim(@PathVariable Long postingId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        String username = userDetails.getUser().getUsername();
        return new ResponseEntity<>(jjimService.jjimUp(postingId , username), HttpStatus.OK);
    }

//    @GetMapping("/api/posts/jjim/{jjimId}")
//    public List<JjimResponseDto> showJjim(@PathVariable Long postingId) {
//        return jjimService.getJjim(postingId);
//    }
//

}
