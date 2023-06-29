package com.grimpan.drawingdiary.repository;

import com.grimpan.drawingdiary.domain.Diary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiaryRepository extends JpaRepository<Diary, Long> {
}
