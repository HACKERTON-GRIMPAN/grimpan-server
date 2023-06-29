package com.grimpan.drawingdiary.dto;

import com.grimpan.drawingdiary.domain.Diary;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
public class DiaryWriteResponse {
    private Long id;
    private String content;
    private Timestamp createdDate;
    private Long userId;

    public static DiaryWriteResponse of(Diary diary){
        return new DiaryWriteResponse(diary.getId(), diary.getContent(), diary.getCreatedDate(), diary.getUser().getId());
    }
}
