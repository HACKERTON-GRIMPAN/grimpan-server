package com.grimpan.drawingdiary.repository;

import com.grimpan.drawingdiary.domain.Diary;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.jpa.repository.JpaRepository;

@Hidden
public interface DiaryRepository extends JpaRepository<Diary, Long> {
}
