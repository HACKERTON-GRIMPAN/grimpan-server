package com.grimpan.emodiary.service;

import com.grimpan.emodiary.domain.LoginProvider;
import com.grimpan.emodiary.domain.User;
import com.grimpan.emodiary.domain.type.AuthenticationProvider;
import com.grimpan.emodiary.domain.type.UserRole;
import com.grimpan.emodiary.dto.SignUpRequestDto;
import com.grimpan.emodiary.exception.ErrorCode;
import com.grimpan.emodiary.exception.UserException;
import com.grimpan.emodiary.repository.LoginProviderRepository;
import com.grimpan.emodiary.repository.UserRepository;
import com.grimpan.emodiary.security.jwt.JwtProvider;
import com.grimpan.emodiary.unit.Oauth2Util;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.beans.Transient;
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


    @Transactional
    public Map<String, String> login(String authorizationStr, AuthenticationProvider provider) {
        // 소셜 인증 서버에서 소셜 아이디 받기
        String socialId = oauth2Util.getSocialId(authorizationStr, provider);

        // 해당 소셜 아이디로 로그인 한 사람 확인(없다면 User Exception 발생)
        User loginUser = loginProviderRepository.findBySocialIdAndProvider(socialId, provider)
                .orElseThrow(() -> new UserException(ErrorCode.NOT_FOUND_SIGNUP_HISTORY)).getUser();

        // JwtToken 생성, 기존 Refresh Token 탐색
        Map<String, String> jwt = jwtProvider.createTotalToken(loginUser.getId(), loginUser.getRole());
        loginUser.updateRefreshToken(jwt.get("refresh_token"));
        loginUser.updateOnline();

        return jwt;
    }

    @Transactional
    public Map<String, String> signup(String authorizationStr, AuthenticationProvider provider, SignUpRequestDto requestDto) {
        // 회원가입을 위한 User
        User signUpUser = null;

        // 만약 기존 유저라면 찾아서 넣고, 새로운 유저라면 Row를 만들어준다.
        if (requestDto.getName() == null) {
            signUpUser = userRepository.findByPhoneNumber(requestDto.getPhoneNumber().replace("-", ""))
                    .orElseThrow(() -> new UserException(ErrorCode.NOT_FOUND_SIGNUP_HISTORY));
        } else {
            signUpUser = userRepository.save(User.builder()
                    .name(requestDto.getName())
                    .nickname(requestDto.getNickname())
                    .phoneNumber(requestDto.getPhoneNumber().replace("-", ""))
                    .role(provider.equals(AuthenticationProvider.DEFAULT) ? UserRole.ADMIN : UserRole.USER).build());
        }

        // 해당 로그인 유형을 넣어준다.
        loginProviderRepository.save(LoginProvider.builder()
                        .user(signUpUser)
                        .socialId(oauth2Util.getSocialId(authorizationStr, provider))
                        .provider(provider).build());

        // JWT를 발행한다.
        Map<String, String> jwt = jwtProvider.createTotalToken(signUpUser.getId(), signUpUser.getRole());
        signUpUser.updateRefreshToken(jwt.get("refresh_token"));
        signUpUser.updateOnline();

        // 발행한 JWT를 반환한다.
        return jwt;
    }


}
