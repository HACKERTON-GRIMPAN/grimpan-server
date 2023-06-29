package com.grimpan.drawingdiary.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DiaryWriteRequest {
    private String title;
    private String content;
}
