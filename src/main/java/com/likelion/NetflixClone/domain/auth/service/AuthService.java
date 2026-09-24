package com.likelion.NetflixClone.domain.auth.service;

import com.likelion.NetflixClone.domain.auth.dto.LoginRequestDto;
import com.likelion.NetflixClone.domain.auth.dto.SignupRequestDto;
import com.likelion.NetflixClone.domain.auth.dto.TokenResponseDto;
import com.likelion.NetflixClone.domain.user.entity.User;
import com.likelion.NetflixClone.domain.user.repository.UserRepository;
import com.likelion.NetflixClone.global.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    // 1. 회원가입 로직
    @Transactional
    public void signup(SignupRequestDto requestDto) {
        // 이메일 중복 검사
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new IllegalArgumentException("이미 가입된 이메일입니다.");
        }

        // 비밀번호 암호화 후 User 엔티티 생성
        User user = User.builder()
                .email(requestDto.getEmail())
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .nickname(requestDto.getNickname())
                .role(requestDto.getRole()) // ROLE_USER 또는 ROLE_ADMIN
                .build();

        userRepository.save(user);
    }

    // 2. 로그인 및 JWT 토큰 발급 로직
    public TokenResponseDto login(LoginRequestDto requestDto) {
        // 이메일 존재 여부 확인
        User user = userRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 이메일입니다."));

        // 비밀번호 일치 여부 검증
        if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // JWT 토큰 생성
        String accessToken = jwtProvider.createToken(user.getEmail(), user.getRole().name());

        return TokenResponseDto.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .build();
    }
}