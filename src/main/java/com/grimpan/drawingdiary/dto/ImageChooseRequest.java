package com.grimpan.drawingdiary.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ImageChooseRequest {
    private String artName;
    private boolean isSelected;
}
