package com.grimpan.emodiary.unit;

import com.grimpan.emodiary.exception.CommonException;
import com.grimpan.emodiary.type.ELoginProvider;
import com.grimpan.emodiary.exception.ErrorCode;
import com.nimbusds.jose.shaded.gson.JsonElement;
import com.nimbusds.jose.shaded.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
@RequiredArgsConstructor
public class Oauth2Util {
    /* 카카오 소셜 로그인용 Data  */
    @Value("${client.provider.kakao.user-info-uri}")
    private String KAKAO_USERINFO_URL;
    @Value("${client.registration.kakao.client-id}")
    private String KAKAO_CLIENT_ID;
    @Value("${client.registration.kakao.client-secret}")
    private String KAKAO_CLIENT_SECRET;
    @Value("${client.registration.kakao.redirect-uri}")
    private String KAKAO_REDIRECT_URL;

    /* 구글 소셜 로그인용 Data  */
    @Value("${client.provider.google.user-info-uri}")
    private String GOOGLE_USERINFO_URL;
    @Value("${client.registration.google.client-id}")
    private String GOOGLE_CLIENT_ID;
    @Value("${client.registration.google.client-secret}")
    private String GOOGLE_CLIENT_SECRET;
    @Value("${client.registration.google.redirect-uri}")
    private String GOOGLE_REDIRECT_URL;

    /* 애플 소셜 로그인용 추가 예정  */

    /* WebClient 방식으로 Upgrade 예정 */
    private final static RestTemplate restTemplate = new RestTemplate();

    public String getSocialId(String authorizationStr, ELoginProvider provider) {
        String socialId = null;
        switch (provider) {
            case KAKAO -> {
                socialId = getKakaoUserInformation(authorizationStr);
            }
            case GOOGLE -> {
                socialId = getGoogleUserInformation(authorizationStr);
            }
            case APPLE -> {
                socialId = getAppleUserInformation(authorizationStr);
            }
        }

        // 소셜 서버에 User Data 존재 여부 확인
        if (socialId == null) { throw new CommonException(ErrorCode.NOT_FOUND_USER, null); }

        return socialId;
    }

    private String getKakaoUserInformation(String accessToken) {
        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.add("Authorization", "Bearer "+ accessToken);
        httpHeaders.add("Content-type","application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String,String >> kakaoProfileRequest= new HttpEntity<>(httpHeaders);

        ResponseEntity<String> response = restTemplate.exchange(
                KAKAO_USERINFO_URL,
                HttpMethod.POST,
                kakaoProfileRequest,
                String.class
        );

        // 토큰을 사용하여 사용자 정보 추출
        JsonElement element = JsonParser.parseString(response.getBody());

        return element.getAsJsonObject().get("id").getAsString();
    }

    private String getGoogleUserInformation(String accessToken) {
        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.add("Authorization", "Bearer "+ accessToken);
        httpHeaders.add("Content-type","application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String, String>> googleProfileRequest= new HttpEntity<>(httpHeaders);

        ResponseEntity<String> response = restTemplate.exchange(
                GOOGLE_USERINFO_URL,
                HttpMethod.GET,
                googleProfileRequest,
                String.class
        );

        // 토큰을 사용하여 사용자 정보 추출
        JsonElement element = JsonParser.parseString(response.getBody());

        return element.getAsJsonObject().get("id").getAsString();
    }

    private String getAppleUserInformation(String authorizationStr) {
        return null;
    }
}
