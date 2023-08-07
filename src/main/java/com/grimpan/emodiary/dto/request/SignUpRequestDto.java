package com.grimpan.emodiary.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;

@Getter
public class SignUpRequestDto {
    @Max(30)
    private final String name;

    @Max(15)
    private final String nickname;

    @Max(13)
    @Pattern(regexp = "^01(?:0|1|[6-9])[.-]?(\\d{3}|\\d{4})[.-]?(\\d{4})$", message = "10 ~ 11 자리의 숫자만 입력 가능합니다.")
    private final String phoneNumber;

    @Builder
    public SignUpRequestDto(String name, String nickname, String phoneNumber) {
        this.name = name;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
    }
}

