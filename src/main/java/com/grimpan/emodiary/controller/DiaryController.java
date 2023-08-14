package com.grimpan.emodiary.controller;

import com.grimpan.emodiary.dto.request.DiaryWriteRequest;
import com.grimpan.emodiary.dto.response.DiaryResponse;
import com.grimpan.emodiary.dto.response.DiaryWriteResponse;
import com.grimpan.emodiary.dto.response.EmotionResponse;
import com.grimpan.emodiary.dto.response.ResponseDto;
import com.grimpan.emodiary.service.DiaryService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;


@Api(tags = "일기 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/diary")
public class DiaryController {
    private final DiaryService diaryService;

    @Operation(summary = "일기 작성", description = "작성 후 reponse로 그림 4개 return")
    @PostMapping("")
    public ResponseDto<DiaryWriteResponse> createDiary(HttpServletRequest servletRequest, @RequestBody DiaryWriteRequest request) throws IOException {
        return ResponseDto.ok(diaryService.create(request, (Long)servletRequest.getAttribute("USER_ID")));
    }

    @Operation(summary = "일기 상세 조회", description = "id로 일기 상세 조회")
    @GetMapping(value = "/{id}")
    public ResponseDto<DiaryResponse> getOneDiary(@PathVariable Long id, HttpServletRequest servletRequest) {
        return ResponseDto.ok(diaryService.getOneDiary(id, (Long)servletRequest.getAttribute("USER_ID")));
    }

    @Operation(summary = "월단위 일기 조회", description = "구간에 해당하는 이미지 List 보여주기")
    @GetMapping(value = "/monthly")
    public ResponseDto<?> getImageListForMonth(HttpServletRequest servletRequest, @RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate){
        List<Map.Entry<String, String>> diaryMap = diaryService.getImageListByDateRange(startDate, endDate, (Long)servletRequest.getAttribute("USER_ID"));
        return ResponseDto.ok(diaryMap);
    }

    @Operation(summary = "월 감정 정보 조회", description = "해당 월의 일별 감정점수와 감정분포 조회")
    @GetMapping(value = "/emotionInfo/monthly")
    public ResponseDto<EmotionResponse> getEmotionInfotForMonthly(@RequestParam("date") String date,HttpServletRequest servletRequest) {
        return ResponseDto.ok(diaryService.getEmotionInfoForMonthly(date,(Long)servletRequest.getAttribute("USER_ID")));
    }
////
////    @Operation(summary = "이미지 선택", description = "4개의 이미지 중 사용자가 선택한 이미지만 남기기")
////    @PutMapping(value = "/{id}/images")
////    public ResponseEntity<DiaryResponse> chooseImage(@PathVariable Long id, @RequestBody List<ImageChooseRequest> requestList) throws IOException {
////        DiaryResponse response = diaryService.chooseImage(id, requestList);
////        return ResponseEntity.ok().body(response);
////    }
////
////    @Operation(summary = "이미지 다운로드", description = "해당 UUID를 가진 이미지 보여주기")
////    @GetMapping(value = "/images")
////    public ResponseEntity<?> downloadImage(@RequestParam("uuid") String fileName, @RequestParam("size") Integer size) throws Exception {
////        byte[] imageData = diaryService.downloadImage(fileName, size);
////
////        if (imageData.length == 0)
////            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
////                    .body("fail");
////        else
////            return ResponseEntity.status(HttpStatus.OK)
////                    .contentType(MediaType.valueOf("image/png"))
////                    .body(imageData);
////    }
////
////

}
