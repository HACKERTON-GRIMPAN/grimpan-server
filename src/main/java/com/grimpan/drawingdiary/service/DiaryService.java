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
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Base64;
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
        Diary diary = Diary.builder()
                .content(request.getContent())
                .title(request.getTitle()).build();
        Diary saved = diaryRepository.save(diary);
        List<String> imgNameList = makeImageWithAI(request.getContent());
        return new DiaryWriteResponse(saved.getId(), ImageToUrl(imgNameList));
    }

    public List<String> ImageToUrl(List<String> imgNameList) throws IOException {
        List<String> imageResponses = new ArrayList<>();
        for(String imgName : imgNameList){
            imageResponses.add(urlPath + "diary/images?uuid=" + imgName);
        }
        return imageResponses;
    }

    //이미지 생성
    private List<String> makeImageWithAI(String content){
        String tokens = diaryUnit.getTokenByDiary(content);
        return diaryUnit.getImgNameList(tokens);
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

    public byte[] downloadImage(String UuidName) throws IOException {
        String filePath = imagePath;

        filePath = imagePath + UuidName;

        byte[] images = Files.readAllBytes(new File(filePath).toPath());
        return images;
    }
}
