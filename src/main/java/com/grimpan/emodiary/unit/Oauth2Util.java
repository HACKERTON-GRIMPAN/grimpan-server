package com.grimpan.emodiary.unit;

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
    @Value("${client.provider.kakao.user-info-uri}")
    private String KAKAO_USERINFO_URL;
    @Value("${client.registration.kakao.client-id}")
    private String KAKAO_CLIENT_ID;
    @Value("${client.registration.kakao.client-secret}")
    private String KAKAO_CLIENT_SECRET;
    @Value("${client.registration.kakao.redirect-uri}")
    private String KAKAO_REDIRECT_URL;

    private final static RestTemplate restTemplate = new RestTemplate();

    public String getKakaoUserInformation(String accessToken) {
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
}
