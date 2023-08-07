package com.grimpan.emodiary.service;

import com.grimpan.emodiary.domain.LoginProvider;
import com.grimpan.emodiary.domain.User;
import com.grimpan.emodiary.exception.CommonException;
import com.grimpan.emodiary.type.ELoginProvider;
import com.grimpan.emodiary.type.ESIGNUP;
import com.grimpan.emodiary.type.EUserRole;
import com.grimpan.emodiary.dto.request.SignUpRequestDto;
import com.grimpan.emodiary.exception.ErrorCode;
import com.grimpan.emodiary.repository.LoginProviderRepository;
import com.grimpan.emodiary.repository.UserRepository;
import com.grimpan.emodiary.security.jwt.JwtProvider;
import com.grimpan.emodiary.unit.Oauth2Util;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final Oauth2Util oauth2Util;
    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;
    private final LoginProviderRepository loginProviderRepository;


    @Transactional
    public Map<String, String> login(String authorizationStr, ELoginProvider provider) {
        // 소셜 인증 서버에서 소셜 아이디 받기
        String socialId = oauth2Util.getSocialId(authorizationStr, provider);

        // 해당 소셜 아이디로 로그인 한 사람 확인(없다면 User Exception 발생)
        User loginUser = loginProviderRepository.findBySocialIdAndProvider(socialId, provider)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_SIGNUP_HISTORY, null)).getUser();

        // JwtToken 생성, 기존 Refresh Token 탐색
        Map<String, String> jwt = jwtProvider.createTotalToken(loginUser.getId(), loginUser.getRole());
        loginUser.updateRefreshToken(jwt.get("refresh_token"));
        loginUser.updateOnline();

        return jwt;
    }

    @Transactional
    public Map<String, String> signup(ESIGNUP signUpType, String authorizationStr, ELoginProvider provider, SignUpRequestDto requestDto) {
        User user = null;

        // 신규 유저라면 새롭게 user 생성
        if (signUpType.equals(ESIGNUP.NEW)) {
            user = userRepository.save(User.builder()
                    .name(requestDto.getName())
                    .nickname(requestDto.getNickname())
                    .phoneNumber(requestDto.getPhoneNumber().replace("-", ""))
                    .role(EUserRole.USER).build());
        }
        // 기존 유저라면 user 검색
        else {
            userRepository.findByPhoneNumber(requestDto.getPhoneNumber().replace("-", ""))
                    .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_SIGNUP_HISTORY, null));
        }

        // 해당 로그인 유형을 넣어준다.
        loginProviderRepository.save(LoginProvider.builder()
                        .user(user)
                        .socialId(oauth2Util.getSocialId(authorizationStr, provider))
                        .provider(provider).build());

        // JWT를 발행한다.
        Map<String, String> jwt = jwtProvider.createTotalToken(user.getId(), user.getRole());
        user.updateRefreshToken(jwt.get("refresh_token"));
        user.updateOnline();

        // 발행한 JWT를 반환한다.
        return jwt;
    }

    public Boolean checkPhoneNumber(String phoneNumber) {
        return userRepository.existsByPhoneNumber(phoneNumber);
    }
}
