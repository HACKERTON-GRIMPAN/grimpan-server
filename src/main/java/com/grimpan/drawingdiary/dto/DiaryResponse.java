package com.grimpan.drawingdiary.dto;

import com.grimpan.drawingdiary.domain.Diary;
import com.grimpan.drawingdiary.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;

@AllArgsConstructor
@Getter
public class DiaryResponse {
    private Long id;
    private User user;
    private String artName; //그림 이름

    private String content;
    private Timestamp createdDate;

    public static DiaryResponse of(Diary diary){
        return new DiaryResponse(diary.getId(), diary.getUser(), diary.getArtName(), diary.getContent(), diary.getCreatedDate());
    }
}
