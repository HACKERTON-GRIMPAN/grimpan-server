package com.grimpan.emodiary.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
public class ImageChooseRequest {
    private String artName;
    private Boolean isSelected;
}
