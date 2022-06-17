package com.carrot.carrot_back.service;

import com.carrot.carrot_back.dto.SignupRequestDto;
import com.carrot.carrot_back.model.User;
import com.carrot.carrot_back.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

    public boolean checkId(SignupRequestDto requestDto){
        String username = requestDto.getUsername();
        return (!userRepository.findByUsername(username).isPresent());
    }
}