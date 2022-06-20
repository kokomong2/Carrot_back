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

    @PostMapping("/user/signup/checkid")
    public boolean checkid(@RequestBody SignupRequestDto requestDto){

        return userService.checkId(requestDto);
    }

    @PostMapping("/user/signup/checknickname")
    public boolean checkNickname(@RequestBody SignupRequestDto requestDto){

        return userService.checkNickname(requestDto);
    }
}