package com.grimpan.emodiary.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class DiaryWriteRequest {
    private String title;

    //추후 글자수 제한 추가
    private String content;
}
