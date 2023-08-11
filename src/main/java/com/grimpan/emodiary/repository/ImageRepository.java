package com.grimpan.emodiary.repository;

import com.grimpan.emodiary.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
