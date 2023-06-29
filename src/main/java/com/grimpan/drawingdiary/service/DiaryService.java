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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class DiaryService {

    @Value("${spring.image.path}")
    private String imagePath;
    private final DiaryRepository diaryRepository;
    private final DiaryUnit diaryUnit;

    @Transactional
    public DiaryWriteResponse create(DiaryWriteRequest request) throws IOException {
        Diary diary = Diary.builder()
                .content(request.getContent())
                .title(request.getTitle()).build();
        Diary saved = diaryRepository.save(diary);
        List<String> imgNameList = makeImageWithAI(request.getContent());
        return new DiaryWriteResponse(saved.getId(),ImageToBase64(imgNameList));
    }

    public List<ImageResponse> ImageToBase64(List<String> imgNameList) throws IOException {
        List<ImageResponse> imageResponses = new ArrayList<>();
        for(String imgName:imgNameList){
            String fullPath = imagePath +  imgName;
            byte[] imageByte = readImageFile(fullPath);
            String base64Data = Base64.getEncoder().encodeToString(imageByte);
            ImageResponse imageResponse = new ImageResponse(imgName, base64Data);
            imageResponses.add(imageResponse);
        }
        return imageResponses;
    }

    //이미지 생성
    private List<String> makeImageWithAI(String content){
        String englishContent = diaryUnit.changeLanguage(content);
        String firstTokens = diaryUnit.getFirstTokens(englishContent);
        String secondTokens = diaryUnit.getSecondTokens(firstTokens);
        return diaryUnit.getImgNameList(secondTokens);
    }

    private byte[] readImageFile(String filePath) throws IOException {
        File imageFile = new File(filePath);
        FileInputStream fileInputStream = new FileInputStream(imageFile);

        byte[] imageData = new byte[(int) imageFile.length()];
        fileInputStream.read(imageData);
        fileInputStream.close();

        return imageData;
    }

    public DiaryResponse getOneDiary(Long id) {
        Diary diary = diaryRepository.findById(id).orElseThrow(() -> new DiaryException(ErrorCode.DIARY_NOT_FOUND));
        return DiaryResponse.of(diary);
    }

    @Transactional
    public DiaryResponse chooseImage(Long id, List<ImageChooseRequest> requestList) throws IOException {
        String selectedImage = "";
        for(ImageChooseRequest request : requestList){
            if(!request.isSelected()){
                String imageFullPath = imagePath + request.getArtName();
                File file = new File(imageFullPath);
                if(file.delete()){
                    log.info(imageFullPath +"가 삭제되었습니다.");
                }
            }else{
                selectedImage = request.getArtName();
            }
        }

        String imageFullPath = imagePath + selectedImage;
        byte[] imageByte = readImageFile(imageFullPath);
        String base64Data = Base64.getEncoder().encodeToString(imageByte);

        Diary diary = diaryRepository.findById(id).orElseThrow(() -> new DiaryException(ErrorCode.DIARY_NOT_FOUND));
        diary.setArtName(selectedImage);
        return new DiaryResponse(diary.getId(), diary.getTitle(),base64Data, diary.getContent());
    }
}
