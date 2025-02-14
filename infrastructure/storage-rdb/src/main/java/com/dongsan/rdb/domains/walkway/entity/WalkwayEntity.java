package com.dongsan.rdb.domains.walkway.entity;

import com.dongsan.core.domains.walkway.Author;
import com.dongsan.core.domains.walkway.CourseInfo;
import com.dongsan.core.domains.walkway.CreateWalkway;
import com.dongsan.core.domains.walkway.Stat;
import com.dongsan.core.domains.walkway.Walkway;
import com.dongsan.core.domains.walkway.enums.ExposeLevel;
import com.dongsan.rdb.domains.common.entity.BaseEntity;
import com.dongsan.rdb.domains.member.MemberEntity;
import com.dongsan.rdb.domains.review.RatingCount;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.util.List;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;

@Entity
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

    @Column(columnDefinition = "json")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<String> hashtags;

    protected WalkwayEntity() {}

    // 연관관계 매핑도 생성 시 매핑합니다.
    private WalkwayEntity(String name, Double distance, Integer time, ExposeLevel exposeLevel, Point startLocation,
                          Point endLocation, String memo, LineString course, String courseImageUrl, MemberEntity memberEntity, List<String> hashtags){
        this.name = name;
        this.distance = distance;
        this.time = time;
        this.exposeLevel = exposeLevel;
        this.startLocation = startLocation;
        this.endLocation = endLocation;
        this.memo = memo;
        this.course = course;
        this.courseImageUrl = courseImageUrl;
        this.hashtags = hashtags;

        // 연관관계 매핑
        this.memberEntity = memberEntity;

        // 생성 시 default 값
        this.likeCount = 0;
        this.reviewCount = 0;
        this.rating = 0.0;
    }

    public WalkwayEntity(CreateWalkway createWalkway, MemberEntity memberEntity){
        // 경로
        LineString course = createWalkway.course();
        Point startLocation = createWalkway.startLocation();
        Point endLocation = createWalkway.endLocation();

        course.setSRID(4326);
        startLocation.setSRID(4326);
        endLocation.setSRID(4326);

        this.name = createWalkway.name();
        this.distance = createWalkway.distance();
        this.time = createWalkway.time();
        this.exposeLevel = createWalkway.exposeLevel();
        this.startLocation = startLocation;
        this.endLocation = endLocation;
        this.memo = createWalkway.memo();
        this.course = course;
        this.courseImageUrl = createWalkway.courseImageUrl();
        this.hashtags = createWalkway.hashtags();

        // 연관관계 매핑
        this.memberEntity = memberEntity;

        // 생성 시 default 값
        this.likeCount = 0;
        this.reviewCount = 0;
        this.rating = 0.0;
    }

    public Walkway toWalkway() {
        CourseInfo courseInfo = new CourseInfo(distance, time, startLocation, endLocation, course, courseImageUrl);
        Author author = memberEntity.toAuthor();
        Stat stat = new Stat(likeCount, reviewCount, rating);
        return new Walkway(id, name, getCreatedAt(), memo, stat, hashtags, courseInfo, author, exposeLevel);
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

    public void increaseLikeCount() {
        this.likeCount++;
    }

    public void decreaseLikeCount() {
        this.likeCount--;
    }

    public void updateWalkway(String name, String memo, ExposeLevel exposeLevel, List<String> hashtags) {
        this.name = name;
        this.memo = memo;
        this.exposeLevel = exposeLevel;
        this.hashtags = hashtags;
    }

    public Long getId() {
        return id;
    }

    public Integer getLikeCount() {
        return likeCount;
    }

    public Double getRating() {
        return rating;
    }
}
