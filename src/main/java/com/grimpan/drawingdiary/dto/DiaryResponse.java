package com.grimpan.drawingdiary.dto;

import com.grimpan.drawingdiary.domain.Diary;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;

@AllArgsConstructor
@Getter
public class DiaryResponse {
    private Long id;
    private String artName; //그림 이름
    private String content;
    private Timestamp createdDate;

    public static DiaryResponse of(Diary diary){
        return new DiaryResponse(diary.getId(), diary.getArtName(), diary.getContent(), diary.getCreatedDate());
    }
}
