package com.grimpan.drawingdiary.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class DiaryWriteResponse {
    private Long id;
    private List<String> imageResponses;
}
