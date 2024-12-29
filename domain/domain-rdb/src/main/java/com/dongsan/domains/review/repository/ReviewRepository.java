package com.dongsan.domains.review.repository;

import com.dongsan.domains.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long>{
    boolean existsByIdAndMemberId(Long reviewId, Long memberId);

    Integer countByWalkwayId(Long walkwayId);
}
