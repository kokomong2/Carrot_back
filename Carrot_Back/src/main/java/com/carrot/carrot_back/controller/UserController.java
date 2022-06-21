package com.carrot.carrot_back.controller;

import com.carrot.carrot_back.dto.SignupRequestDto;
import com.carrot.carrot_back.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class
UserController {

    private final UserService userService;

    //회원가입
    @PostMapping("/user/signup")
    public ResponseEntity registerUser(@RequestBody SignupRequestDto requestDto) {

        try { // 회원가입 진행 성공시 true
            userService.registerUser(requestDto);
            return new ResponseEntity("회원가입 성공", HttpStatus.OK);
        } catch (Exception e) { // 에러나면 false
            return new ResponseEntity("회원가입 실패", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/user/signup/checkid/{username}")
    public ResponseEntity checkid(@PathVariable String username){

        return userService.checkId(username);
    }

    @GetMapping("/user/signup/checknickname/{nickname}")
    public ResponseEntity checkNickname(@PathVariable String nickname){

        return userService.checkNickname(nickname);
    }
}