package com.grimpan.drawingdiary.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class DiaryWriteRequest {
    private String title;
    private String content;
}
