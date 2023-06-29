package com.grimpan.drawingdiary.controller;

import com.grimpan.drawingdiary.dto.DiaryResponse;
import com.grimpan.drawingdiary.dto.DiaryWriteRequest;
import com.grimpan.drawingdiary.dto.DiaryWriteResponse;
import com.grimpan.drawingdiary.dto.ImageChooseRequest;
import com.grimpan.drawingdiary.service.DiaryService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;


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
    @PutMapping(value = "/{id}/images")
    public ResponseEntity<DiaryResponse> chooseImage(@PathVariable Long id, @RequestBody List<ImageChooseRequest> requestList) throws IOException {
        DiaryResponse response = diaryService.chooseImage(id, requestList);
        return ResponseEntity.ok().body(response);
    }

    @Operation(summary = "이미지 다운로드", description = "해당 UUID를 가진 이미지 보여주기")
    @GetMapping(value = "/images")
    public ResponseEntity<?> downloadImage(@RequestParam("uuid") String fileName) throws Exception {
        byte[] imageData = diaryService.downloadImage(fileName);

        if (imageData.length == 0)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("fail");
        else
            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.valueOf("image/png"))
                    .body(imageData);
    }
}
