package com.dongsan.rdb.domains.walkway.repository;

import com.dongsan.rdb.domains.walkway.entity.WalkwayHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WalkwayHistoryJpaRepository extends JpaRepository<WalkwayHistoryEntity, Long> {

}
