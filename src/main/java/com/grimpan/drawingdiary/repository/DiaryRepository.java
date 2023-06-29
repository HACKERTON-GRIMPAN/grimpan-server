package com.grimpan.drawingdiary.repository;

import com.grimpan.drawingdiary.domain.Diary;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface DiaryRepository extends JpaRepository<Diary, Long> {

    @Query(value = "select d from Diary d where d.id = :id")
    Optional<Diary> findForSetImage(@Param("id") Long diaryId);
}
