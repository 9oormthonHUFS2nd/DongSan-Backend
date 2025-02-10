package com.dongsan.rdb.domains.image.repository;

import com.dongsan.rdb.domains.image.entity.ImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageJpaRepository extends JpaRepository<ImageEntity, Long> {
}
