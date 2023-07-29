package com.grimpan.emodiary.service;

import com.grimpan.emodiary.domain.User;
import com.grimpan.emodiary.domain.type.AuthenticationProvider;
import com.grimpan.emodiary.exception.ErrorCode;
import com.grimpan.emodiary.exception.UserException;
import com.grimpan.emodiary.repository.LoginProviderRepository;
import com.grimpan.emodiary.repository.UserRepository;
import com.grimpan.emodiary.security.jwt.JwtProvider;
import com.grimpan.emodiary.unit.Oauth2Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final Oauth2Util oauth2Util;
    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;
    private final LoginProviderRepository loginProviderRepository;


    public Map<String, String> login(String authorizationStr, AuthenticationProvider provider) {
        String socialId = getSocialId(authorizationStr, provider);

        // 해당 소셜 아이디로 로그인 한 사람 확인
        User loginUser = loginProviderRepository.findBySocialIdAndProvider(socialId, provider)
                .orElseThrow(() -> new UserException(ErrorCode.NOT_FOUND_SIGNUP_HISTORY)).getUser();

        // JwtToken 생성, 기존 Refresh Token 탐색
        Map<String, String> jwt = jwtProvider.createTotalToken(loginUser.getId(), loginUser.getRole());
        loginUser.updateRefreshToken(jwt.get("refresh_token"));
        loginUser.updateOnline();

        return jwt;
    }

    private String getSocialId(String authorizationStr, AuthenticationProvider provider) {
        String socialId = null;
        switch (provider) {
            case KAKAO -> {
                socialId = oauth2Util.getKakaoUserInformation(authorizationStr);
            }
            case GOOGLE -> {
            }
            case APPLE -> {
            }
            case DEFAULT -> {
            }
        }

        // 소셜 서버에 User Data 존재 여부 확인
        if (socialId == null) { throw new UserException(ErrorCode.NOT_FOUND_USER); }

        return socialId;
    }
}
