package com.grimpan.emodiary.service;

import com.grimpan.emodiary.domain.Diary;
import com.grimpan.emodiary.domain.User;
import com.grimpan.emodiary.dto.request.*;
import com.grimpan.emodiary.dto.response.*;
import com.grimpan.emodiary.exception.CommonException;
import com.grimpan.emodiary.exception.DiaryException;
import com.grimpan.emodiary.exception.ErrorCode;
import com.grimpan.emodiary.repository.DiaryRepository;
import com.grimpan.emodiary.repository.UserRepository;
import com.grimpan.emodiary.unit.DiaryUnit;
import com.grimpan.emodiary.unit.ImageUtil;
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
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class DiaryService {
    @Value("${spring.image.path}")
    private String imagePath;

    private final DiaryRepository diaryRepository;
    private final UserRepository userRepository;
    private final DiaryUnit diaryUnit;
    private final ImageUtil imageUtil;

    @Transactional
    public DiaryWriteResponse create(DiaryWriteRequest request, Long userId) throws IOException {
        //일기 내용 바탕으로 키워드 추출 후 이미지 4개 생성
        List<String> imgUrlList = diaryUnit.createImgUrlList(request.getContent());
        //일기 내용 바탕으로 감정 분석 (0~100, 단위 : %)
        int emotionScore = diaryUnit.getEmotionScore(request.getContent());

        //로그인 유저 존재하는지 확인
        User user = userRepository.findById(userId).orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER, null));

        Diary diary = diaryRepository.save(Diary.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .user(user)
                .emotionScore(emotionScore)
                .build());

        return new DiaryWriteResponse(diary.getId(), imgUrlList);
    }


    @Transactional(readOnly = true)
    public DiaryResponse getOneDiary(Long id, Long userId) {
        Diary diary = diaryRepository.findById(id).orElseThrow(() -> new DiaryException(ErrorCode.DIARY_NOT_FOUND));
        if(!diary.getUser().getId().equals(userId)){
            throw new CommonException(ErrorCode.ACCESS_DENIED_ERROR, "해당 일기 작성자가 아닙니다.");
        }
        return DiaryResponse.of(diary);
    }
//
//    @Transactional
//    public DiaryResponse chooseImage(Long id, List<ImageChooseRequest> requestList) {
//        String selectedImage = "";
//        for(ImageChooseRequest request : requestList){
//            if(!request.getIsSelected().booleanValue()){
//                String imageFullPath = imagePath + request.getArtName();
//                File file = new File(imageFullPath);
//                if(file.delete()){
//                    log.info(imageFullPath +"가 삭제되었습니다.");
//                }
//            }else{
//                selectedImage = request.getArtName();
//            }
//        }
//
//        //선택된 이미지와 관련 일기 리턴
//        String imageUrlPath = urlPath + "diary/images?uuid=" + selectedImage;
//
//        Diary diary = diaryRepository.findForSetImage(id).orElseThrow(() -> new DiaryException(ErrorCode.DIARY_NOT_FOUND));
//        diary.setArtName(selectedImage);
//        return new DiaryResponse(diary.getId(), diary.getTitle(), imageUrlPath, diary.getContent());
//    }
//
//    public byte[] downloadImage(String UuidName, Integer size) throws IOException {
//        String filePath = imagePath;
//
//        filePath = imagePath + UuidName;
//
//        BufferedImage inputImage = ImageIO.read(new File(filePath));
//
//        BufferedImage outputImage = new BufferedImage(size, size, inputImage.getType());
//
//        Graphics2D graphics2D = outputImage.createGraphics();
//        graphics2D.drawImage(inputImage, 0, 0, size, size, null);
//        graphics2D.dispose();
//
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        ImageIO.write(outputImage, "png", baos);
//        baos.flush();
//
//        return baos.toByteArray();
//    }
//

    @Transactional
    public List<Map.Entry<String, String>> getImageListByDateRange(String startDate, String endDate, Long userId) {
        //구간 내 diary 추출
        List<Diary> diaryList = diaryRepository.findByUserIdAndBetweenCreatedAt(startDate, endDate, userId);

        // startDate부터 endDate까지의 모든 날짜를 순회 >> Default 값 세팅
        LocalDate start = LocalDate.parse(startDate, DateTimeFormatter.ISO_DATE);
        LocalDate end = LocalDate.parse(endDate, DateTimeFormatter.ISO_DATE);
        Map<String, String> imageMap = new HashMap<>();
        for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
            imageMap.put(date.toString(), "");
        }

        // diary id에 해당하는 image추출
        for (Diary diary : diaryList) {
            String date = diary.getCreatedAt().toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            // image base64로 리턴
            String path = imagePath + diary.getImage().getUuidName();
            String imageBase64 = imageUtil.encoder(path);

            imageMap.put(date, imageMap.getOrDefault(date,imageBase64));
        }

        List<Map.Entry<String, String>> responses = new ArrayList<>(imageMap.entrySet());
        return responses;
    }
//
//    public List<Map<Integer, Integer>> getScoreListForWeek() {
//        LocalDate currentDate = LocalDate.now();
//
//        // 현재 날짜에서 제일 가까운 일요일
//        LocalDate firstDay = currentDate.with(DayOfWeek.SUNDAY);
//        // 현재 날짜에서 제일 가까운 토요일
//        LocalDate lastDay = currentDate.with(DayOfWeek.SATURDAY);
//
//        if (!lastDay.isAfter(firstDay)) {
//            firstDay = lastDay.minusDays(6);
//        } else {
//            lastDay = firstDay.plusDays(6);
//        }
//
//        Date startDate = java.sql.Date.valueOf(firstDay);
//        Date endDate = java.sql.Date.valueOf(lastDay.plusDays(1));
//
//        List<Diary> diaryList = diaryRepository.findForMonthList(new Timestamp(startDate.getTime()),
//                new Timestamp(endDate.getTime()));
//
//        List<Map<Integer, Integer>> responseList = new ArrayList<>();
//        for (int i = 0; i < 7; i++) {
//            Map<Integer, Integer> map = new HashMap<>();
//            map.put(i, null);
//            responseList.add(map);
//        }
//
//        for (Diary diary : diaryList) {
//            int index = diary.getCreatedDate().toLocalDateTime().getDayOfMonth() - firstDay.getDayOfMonth();
//
//            Map<Integer, Integer> map = new HashMap<>();
//            map.put(index, diary.getEmotionScore().intValue());
//
//            responseList.set(index, map);
//        }
//
//        return responseList;
//    }
}
