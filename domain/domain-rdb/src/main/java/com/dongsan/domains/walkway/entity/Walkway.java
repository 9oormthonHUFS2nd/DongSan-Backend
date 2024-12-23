package com.dongsan.domains.walkway.entity;

import com.dongsan.domains.common.entity.BaseEntity;
import com.dongsan.domains.hashtag.entity.HashtagWalkway;
import com.dongsan.domains.member.entity.Member;
import com.dongsan.domains.walkway.enums.ExposeLevel;
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
public class Walkway extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

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
    private Walkway(String name, Double distance, Integer time, ExposeLevel exposeLevel, Point startLocation,
                    Point endLocation, String memo, LineString course, String courseImageUrl, Member member){
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
        this.member = member;

        // 생성 시 default 값
        this.likeCount = 0;
        this.reviewCount = 0;
        this.rating = 0.0;
    }

    public void updateRatingAndReviewCount(Byte inputRating) {
        this.rating = (this.rating + inputRating) / 2;
        this.reviewCount++;
    }

    public void addHashtagWalkway(HashtagWalkway hashtagWalkway) {
        this.hashtagWalkways.add(hashtagWalkway);
    }
    public void addLikedWalkway(LikedWalkway likedWalkway) {
        this.likedWalkways.add(likedWalkway);
        this.likeCount++;
    }
    public void removeLikedWalkway() {
        this.likeCount++;
    }
    public void removeAllHashtagWalkway() {
        this.hashtagWalkways = new ArrayList<>();
    }
    public void updateWalkway(String name, String memo, String exposeLeven) {
        this.name = name;
        this.memo = memo;
        this.exposeLevel = ExposeLevel.getExposeLevelByDescription(exposeLeven);
    }
}
