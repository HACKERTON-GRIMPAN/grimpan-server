package com.grimpan.drawingdiary.controller;

import com.grimpan.drawingdiary.dto.DiaryResponse;
import com.grimpan.drawingdiary.dto.DiaryWriteRequest;
import com.grimpan.drawingdiary.dto.DiaryWriteResponse;
import com.grimpan.drawingdiary.service.DiaryService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


@Api(tags = "일기 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/diary")
public class DiaryController {
    private final DiaryService diaryService;

    @Operation(summary = "일기 작성", description = "작성 후 reponse로 그림 4개 return")
    @PostMapping("")
    public ResponseEntity<DiaryWriteResponse> createDiary(@RequestBody DiaryWriteRequest request) throws IOException {
        DiaryWriteResponse response = diaryService.create(request);
        return ResponseEntity.ok()
                .body(response);
    }

    @Operation(summary = "일기 상세 조회", description = "id로 일기 상세 조회")
    @GetMapping(value = "/{id}")
    public ResponseEntity<DiaryResponse> getOneDiary(@PathVariable Long id) {
        DiaryResponse response = diaryService.getOneDiary(id);
        return ResponseEntity.ok().body(response);
    }

    @Operation(summary = "이미지 선택", description = "4개의 이미지 중 사용자가 선택한 이미지만 남기기")
    @GetMapping(value = "/{id}/images/{imageName}")
    public ResponseEntity<DiaryResponse> chooseImage(@PathVariable Long id, @PathVariable String imageName) throws IOException {
        DiaryResponse response = diaryService.chooseImage(id, imageName);
        return ResponseEntity.ok().body(response);
    }
}
