package com.grimpan.emodiary.repository;

import com.grimpan.emodiary.domain.Diary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public interface DiaryRepository extends JpaRepository<Diary, Long> {
    //
//    @Query(value = "select d from Diary d where d.id = :id")
//    Optional<Diary> findForSetImage(@Param("id") Long diaryId);
//
    @Query(value = "select d from Diary d where d.user.id = :userId and d.createdAt between to_timestamp(:startDay, 'YYYY-MM-DD') and to_timestamp(:endDay, 'YYYY-MM-DD') +  0.99999")
    List<Diary> findByUserIdAndBetweenCreatedAt(@Param("startDay") String startDay, @Param("endDay") String endDay, @Param("userId") Long userId);
}
