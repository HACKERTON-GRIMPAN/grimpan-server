package com.grimpan.drawingdiary.service;

import com.grimpan.drawingdiary.domain.Diary;
import com.grimpan.drawingdiary.dto.DiaryWriteRequest;
import com.grimpan.drawingdiary.dto.DiaryWriteResponse;
import com.grimpan.drawingdiary.repository.DiaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class DiaryService {
    private final DiaryRepository diaryRepository;

    @Transactional
    public DiaryWriteResponse create(DiaryWriteRequest request) {
        Diary diary = Diary.builder().content(request.getContent()).build();
        Diary saved = diaryRepository.save(diary);
        return DiaryWriteResponse.of(saved);
    }
}
