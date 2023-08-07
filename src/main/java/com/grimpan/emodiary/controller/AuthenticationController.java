package com.grimpan.emodiary.controller;

import com.grimpan.emodiary.exception.CommonException;
import com.grimpan.emodiary.exception.ErrorCode;
import com.grimpan.emodiary.type.ELoginProvider;
import com.grimpan.emodiary.dto.request.SignUpRequestDto;
import com.grimpan.emodiary.dto.response.ResponseDto;
import com.grimpan.emodiary.security.jwt.JwtProvider;
import com.grimpan.emodiary.service.AuthenticationService;
import com.grimpan.emodiary.type.ESIGNUP;
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
        return ResponseDto.ok(authenticationService.login(JwtProvider.refineToken(request), ELoginProvider.KAKAO));
    }

    @PostMapping("/google")
    public ResponseDto<Map<String, String>> loginBuGoogle(HttpServletRequest request) {
        return ResponseDto.ok(authenticationService.login(JwtProvider.refineToken(request), ELoginProvider.GOOGLE));
    }

    @PostMapping("/apple")
    public ResponseDto<Map<String, String>> loginByApple(HttpServletRequest request) {
        return ResponseDto.ok(authenticationService.login(JwtProvider.refineToken(request), ELoginProvider.APPLE));
    }

    @PostMapping("/signup")
    public ResponseDto<Map<String, String>> signUp(HttpServletRequest request,
                                                   @RequestParam("type") ESIGNUP signUpType,
                                                   @RequestParam("provider") ELoginProvider provider,
                                                   @RequestBody SignUpRequestDto requestDto) {
        return ResponseDto.ok(authenticationService.signup(signUpType, JwtProvider.refineToken(request), provider, requestDto));
    }

    @GetMapping("/check")
    public ResponseDto<Boolean> checkPhoneNumber(@RequestBody Map<String, Object> param) {
        if (param.get("phoneNumber") == null) {
            // MethodArgumentNotValidExceptionDto 생성 예정
            throw new CommonException(ErrorCode.NOT_EXIST_PARAM, null);
        }

        return ResponseDto.ok(authenticationService.checkPhoneNumber((String) param.get("phoneNumber")));
    }
}
