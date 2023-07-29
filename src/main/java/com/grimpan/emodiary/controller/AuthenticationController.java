package com.grimpan.emodiary.controller;

import com.grimpan.emodiary.domain.type.AuthenticationProvider;
import com.grimpan.emodiary.dto.SignUpRequestDto;
import com.grimpan.emodiary.exception.ResponseDto;
import com.grimpan.emodiary.security.jwt.JwtProvider;
import com.grimpan.emodiary.service.AuthenticationService;
import io.swagger.annotations.Api;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Api(tags = "소셜로그인 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/kakao")
    public ResponseDto<Map<String, String>> loginByKakao(HttpServletRequest request) {
        return new ResponseDto<>(authenticationService.login(JwtProvider.refineToken(request), AuthenticationProvider.KAKAO));
    }

    @PostMapping("/google")
    public ResponseDto<Map<String, String>> loginBuGoogle(HttpServletRequest request) {
        return new ResponseDto<>(authenticationService.login(JwtProvider.refineToken(request), AuthenticationProvider.GOOGLE));
    }

    @PostMapping("/apple")
    public ResponseDto<Map<String, String>> loginByApple(HttpServletRequest request) {
        return new ResponseDto<>(authenticationService.login(JwtProvider.refineToken(request), AuthenticationProvider.APPLE));
    }

    @PostMapping("/signup")
    public ResponseDto<Map<String, String>> signUp(HttpServletRequest request,
                                                   @RequestParam("provider") AuthenticationProvider provider,
                                                   @RequestBody SignUpRequestDto requestDto) {
        return new ResponseDto<>(authenticationService.signup(JwtProvider.refineToken(request), provider, requestDto));
    }
}
