package com.grimpan.drawingdiary.dto;

import com.grimpan.drawingdiary.domain.Diary;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class DiaryWriteResponse {
    private Long id;
    private String content;
    private Timestamp createdDate;

    public static DiaryWriteResponse of(Diary diary){
        return new DiaryWriteResponse(diary.getId(), diary.getContent(), diary.getCreatedDate());
    }
}
