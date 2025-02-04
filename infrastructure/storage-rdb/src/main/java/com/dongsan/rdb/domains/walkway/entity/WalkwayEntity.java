package com.dongsan.rdb.domains.walkway.entity;

import com.dongsan.rdb.domains.common.entity.BaseEntity;
import com.dongsan.rdb.domains.member.MemberEntity;
import com.dongsan.rdb.domains.review.RatingCount;
import com.dongsan.rdb.domains.walkway.enums.ExposeLevel;
import com.dongsan.rdb.domains.walkway.repository.HashtagWalkway;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WalkwayEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private MemberEntity memberEntity;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Double distance;

    @Column(nullable = false)
    private Integer time; // 초

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ExposeLevel exposeLevel;

    @Column(nullable = false)
    private Point startLocation;

    @Column(nullable = false)
    private Point endLocation;

    private String memo;

    @Column(nullable = false)
    private Integer likeCount;

    @Column(nullable = false)
    private Integer reviewCount;

    @Column(nullable = false)
    private Double rating;

    @Column(nullable = false)
    private LineString course;

    private String courseImageUrl;

    @OneToMany(mappedBy = "walkway", fetch = FetchType.LAZY)
    private List<HashtagWalkway> hashtagWalkways = new ArrayList<>();

    @OneToMany(mappedBy = "walkway", fetch = FetchType.LAZY)
    private List<LikedWalkway> likedWalkways = new ArrayList<>();

    // 연관관계 매핑도 생성 시 매핑합니다.
    @Builder
    private WalkwayEntity(String name, Double distance, Integer time, ExposeLevel exposeLevel, Point startLocation,
                          Point endLocation, String memo, LineString course, String courseImageUrl, MemberEntity memberEntity){
        this.name = name;
        this.distance = distance;
        this.time = time;
        this.exposeLevel = exposeLevel;
        this.startLocation = startLocation;
        this.endLocation = endLocation;
        this.memo = memo;
        this.course = course;
        this.courseImageUrl = courseImageUrl;

        // 연관관계 매핑
        this.memberEntity = memberEntity;

        // 생성 시 default 값
        this.likeCount = 0;
        this.reviewCount = 0;
        this.rating = 0.0;
    }

    public void updateRatingAndReviewCount(List<RatingCount> ratingCounts) {
        // 총 리뷰 수 계산
        this.reviewCount = (int) ratingCounts.stream()
                .mapToLong(RatingCount::count)
                .sum();
        // 평점 평균 계산 (소수점 한자리)
        this.rating = Math.floor(
                ratingCounts.stream()
                        .mapToDouble(ratingCount -> ratingCount.rating() * ratingCount.count())
                        .sum() / this.reviewCount * 10
        ) / 10.0;
    }

    public void addHashtagWalkway(HashtagWalkway hashtagWalkway) {
        this.hashtagWalkways.add(hashtagWalkway);
    }
    public void addLikedWalkway(LikedWalkway likedWalkway) {
        this.likedWalkways.add(likedWalkway);
        this.likeCount++;
    }
    public void removeLikedWalkway() {
        this.likeCount--;
    }
    public void removeAllHashtagWalkway() {
        this.hashtagWalkways = new ArrayList<>();
    }
    public void updateWalkway(String name, String memo, ExposeLevel exposeLevel) {
        this.name = name;
        this.memo = memo;
        this.exposeLevel = exposeLevel;
    }
}
