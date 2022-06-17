package com.carrot.carrot_back.repository;

import com.carrot.carrot_back.model.ImageUrl;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageUrlRepository extends JpaRepository<ImageUrl, Long> {
}
