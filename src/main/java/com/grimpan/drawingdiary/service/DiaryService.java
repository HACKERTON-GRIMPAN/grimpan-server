package com.grimpan.drawingdiary.service;

import com.grimpan.drawingdiary.domain.Diary;
import com.grimpan.drawingdiary.dto.*;
import com.grimpan.drawingdiary.exception.DiaryException;
import com.grimpan.drawingdiary.exception.ErrorCode;
import com.grimpan.drawingdiary.repository.DiaryRepository;
import com.grimpan.drawingdiary.unit.DiaryUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class DiaryService {
    @Value("${spring.image.path}")
    private String imagePath;
    @Value("${spring.url.path}")
    private String urlPath;
    private final DiaryRepository diaryRepository;
    private final DiaryUnit diaryUnit;

    @Transactional
    public DiaryWriteResponse create(DiaryWriteRequest request) throws IOException {
        String keywords = diaryUnit.getTokenByDiary(request.getContent());
        List<String> imgNameList = diaryUnit.getImgNameList(keywords);

        Diary diary = diaryRepository.save(Diary.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .keywords(keywords)
                .emotionScore(diaryUnit.getEmotionScore(request.getContent())).build());
        return new DiaryWriteResponse(diary.getId(), ImageToUrl(imgNameList));
    }

    public List<String> ImageToUrl(List<String> imgNameList) throws IOException {
        List<String> imageResponses = new ArrayList<>();
        for(String imgName : imgNameList){
            imageResponses.add(urlPath + "diary/images?uuid=" + imgName);
        }
        return imageResponses;
    }

    public DiaryResponse getOneDiary(Long id) {
        Diary diary = diaryRepository.findById(id).orElseThrow(() -> new DiaryException(ErrorCode.DIARY_NOT_FOUND));
        return DiaryResponse.of(diary);
    }

    @Transactional
    public DiaryResponse chooseImage(Long id, List<ImageChooseRequest> requestList) {
        String selectedImage = "";
        for(ImageChooseRequest request : requestList){
            if(!request.getIsSelected().booleanValue()){
                String imageFullPath = imagePath + request.getArtName();
                File file = new File(imageFullPath);
                if(file.delete()){
                    log.info(imageFullPath +"가 삭제되었습니다.");
                }
            }else{
                selectedImage = request.getArtName();
            }
        }

        //선택된 이미지와 관련 일기 리턴
        String imageUrlPath = urlPath + "diary/images?uuid=" + selectedImage;

        Diary diary = diaryRepository.findForSetImage(id).orElseThrow(() -> new DiaryException(ErrorCode.DIARY_NOT_FOUND));
        diary.setArtName(selectedImage);
        return new DiaryResponse(diary.getId(), diary.getTitle(), imageUrlPath, diary.getContent());
    }

    public byte[] downloadImage(String UuidName, Integer size) throws IOException {
        String filePath = imagePath;

        filePath = imagePath + UuidName;

        BufferedImage inputImage = ImageIO.read(new File(filePath));

        BufferedImage outputImage = new BufferedImage(size, size, inputImage.getType());

        Graphics2D graphics2D = outputImage.createGraphics();
        graphics2D.drawImage(inputImage, 0, 0, size, size, null);
        graphics2D.dispose();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(outputImage, "png", baos);
        baos.flush();

        return baos.toByteArray();
    }

    public List<Map<Integer, DiaryResponse>> getImageListByMonth() {
        LocalDate currentDate = LocalDate.now();

        int year = currentDate.getYear();  // 현재 연도
        int month = currentDate.getMonthValue();  // 현재 월

        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate firstDay = yearMonth.atDay(1);  // 시작 날짜
        LocalDate lastDay = yearMonth.atEndOfMonth();  // 마지막 날짜

        Date startDate = java.sql.Date.valueOf(firstDay);
        Date endDate = java.sql.Date.valueOf(lastDay);

        List<Diary> diaryList = diaryRepository.findForMonthList(new Timestamp(startDate.getTime()),
                new Timestamp(endDate.getTime()));

        List<Map<Integer, DiaryResponse>> responseList = new ArrayList<>();
        for (int i = 1; i <= lastDay.getDayOfMonth(); i++) {
            Map<Integer, DiaryResponse> map = new HashMap<>();
            map.put(i, null);
            responseList.add(map);
        }

        for (Diary diary : diaryList) {
            Map<Integer, DiaryResponse> map = new HashMap<>();
            map.put(diary.getCreatedDate().toLocalDateTime().getDayOfMonth(),
                    DiaryResponse.builder()
                    .id(diary.getId())
                    .title(diary.getTitle())
                    .urlPath(urlPath + "diary/images?uuid=" + diary.getArtName())
                    .content(diary.getContent()).build());
            responseList.set(diary.getCreatedDate().toLocalDateTime().getDayOfMonth(), map);
        }

        return responseList;
    }

    public List<Map<Integer, Integer>> getScoreListForWeek() {
        LocalDate currentDate = LocalDate.now();

        // 현재 날짜에서 제일 가까운 일요일
        LocalDate firstDay = currentDate.with(DayOfWeek.SUNDAY);
        // 현재 날짜에서 제일 가까운 토요일
        LocalDate lastDay = currentDate.with(DayOfWeek.SATURDAY);
        
        if (!lastDay.isAfter(firstDay)) {
            firstDay = lastDay.minusDays(6);
        } else {
            lastDay = firstDay.plusDays(6);
        }

        Date startDate = java.sql.Date.valueOf(firstDay);
        Date endDate = java.sql.Date.valueOf(lastDay);

        List<Diary> diaryList = diaryRepository.findForMonthList(new Timestamp(startDate.getTime()),
                new Timestamp(endDate.getTime()));

        List<Map<Integer, Integer>> responseList = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            Map<Integer, Integer> map = new HashMap<>();
            map.put(i, null);
            responseList.add(map);
        }

        for (Diary diary : diaryList) {
            int index = diary.getCreatedDate().toLocalDateTime().getDayOfMonth() - firstDay.getDayOfMonth();

            Map<Integer, Integer> map = new HashMap<>();
            map.put(index, diary.getEmotionScore().intValue());

            responseList.set(index, map);
        }

        return responseList;
    }
}
