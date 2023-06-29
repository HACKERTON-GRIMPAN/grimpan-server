package com.grimpan.drawingdiary.dto;

import com.grimpan.drawingdiary.domain.User;
import java.sql.Timestamp;

public class DiaryResponse {
    private Long id;
    private User user;

    private String content;
    private Timestamp createdDate;
}
