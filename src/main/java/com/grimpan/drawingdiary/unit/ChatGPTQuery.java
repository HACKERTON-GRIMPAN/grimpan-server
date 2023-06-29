package com.grimpan.drawingdiary.unit;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ChatGPTQuery {
    private String role;
    private String content;
}
