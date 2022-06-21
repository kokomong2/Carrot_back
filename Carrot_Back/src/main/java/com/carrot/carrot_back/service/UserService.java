package com.carrot.carrot_back.service;

import com.carrot.carrot_back.dto.requestDto.SignupRequestDto;
import com.carrot.carrot_back.model.User;
import com.carrot.carrot_back.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Optional;

@Service
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void registerUser(SignupRequestDto requestDto) {
        // 회원 ID 중복 확인
        String username = requestDto.getUsername();
        String nickname = requestDto.getNickname();
        Optional<User> found = userRepository.findByUsername(username);
        if (found.isPresent()) {
            throw new IllegalArgumentException("중복된 사용자 ID 가 존재합니다.");
        }

        // 패스워드 암호화
        String password = passwordEncoder.encode(requestDto.getPassword());
        String profileImage = requestDto.getProfileImage();
        String location =requestDto.getLocation();

        User user = new User(username,nickname, password,profileImage,location);
        userRepository.save(user);
    }

    public ResponseEntity checkId(String username){

        if(!userRepository.findByUsername(username).isPresent()){
            return new ResponseEntity("사용가능한 아이디입니다.", HttpStatus.OK);
        }else{
            return new ResponseEntity("중복된 아이디입니다", HttpStatus.BAD_REQUEST);
        }

    }

    public ResponseEntity checkNickname(String nickname){
        if(!userRepository.findByUsername(nickname).isPresent()){
            return new ResponseEntity("사용가능한 닉네임입니다.", HttpStatus.OK);
        }else{
            return new ResponseEntity("중복된 닉네임입니다", HttpStatus.BAD_REQUEST);
        }
    }
}