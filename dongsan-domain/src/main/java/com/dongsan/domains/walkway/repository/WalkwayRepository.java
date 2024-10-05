package com.dongsan.domains.walkway.repository;

import com.dongsan.domains.walkway.entity.Walkway;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WalkwayRepository extends JpaRepository<Walkway, Long> {
}
