package com.dongsan.domains.walkway.repository;

import com.dongsan.domains.walkway.entity.WalkwayHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WalkwayHistoryRepository extends JpaRepository<WalkwayHistory, Long> {

}
