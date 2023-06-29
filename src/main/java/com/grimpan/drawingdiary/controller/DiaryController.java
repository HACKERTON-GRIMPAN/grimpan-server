package com.grimpan.drawingdiary.controller;

import com.grimpan.drawingdiary.dto.DiaryWriteRequest;
import com.grimpan.drawingdiary.dto.DiaryWriteResponse;
import com.grimpan.drawingdiary.service.DiaryService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/diary")
@Slf4j
@Api(tags = "일기 API")
@RequiredArgsConstructor
public class DiaryController {
    private final DiaryService diaryService;

    @Operation(summary = "일기 작성")
    @PostMapping("")
    public ResponseEntity<DiaryWriteResponse> createDiary(@ModelAttribute DiaryWriteRequest request) {
        DiaryWriteResponse response = diaryService.create(request);
        return ResponseEntity.ok()
                .body(response);
    }
}
