package com.grimpan.drawingdiary.service;

import com.grimpan.drawingdiary.domain.Diary;
import com.grimpan.drawingdiary.dto.DiaryResponse;
import com.grimpan.drawingdiary.dto.DiaryWriteRequest;
import com.grimpan.drawingdiary.dto.ImageResponse;
import com.grimpan.drawingdiary.exception.DiaryException;
import com.grimpan.drawingdiary.exception.ErrorCode;
import com.grimpan.drawingdiary.repository.DiaryRepository;
import com.grimpan.drawingdiary.unit.DiaryToImageUnit;
import lombok.RequiredArgsConstructor;
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
@Service
public class DiaryService {

    @Value("${spring.image.path}")
    private String imagePath;
    private final DiaryRepository diaryRepository;
    private final DiaryToImageUnit diaryToImageUnit;

    @Transactional
    public List<ImageResponse> create(DiaryWriteRequest request) throws IOException {
        Diary diary = Diary.builder()
                .content(request.getContent())
                .title(request.getTitle()).build();
        diaryRepository.save(diary);
        List<String> imgNameList = makeImageWithAI(request.getContent());
        return ImageToBase64(imgNameList);
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
        String firstTokens = diaryToImageUnit.getFirstTokens(content);
        String secondTokens = diaryToImageUnit.getSecondTokens(firstTokens);
        return diaryToImageUnit.getImgNameList(secondTokens);
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
    public void chooseImage(String imageName) {
        File[] files = new File(imagePath).listFiles();
        for(File f: files){
            if(!f.getName().equals(imageName)){
                f.delete();
            }
        }
    }
}
