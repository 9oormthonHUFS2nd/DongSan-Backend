package com.dongsan.domains.hashtag.repository;

import com.dongsan.domains.hashtag.entity.HashtagWalkway;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HashtagWalkwayRepository extends JpaRepository<HashtagWalkway, Long> {
}
