package com.dongsan.domains.walkway.entity;

import com.dongsan.domains.common.entity.BaseEntity;
import com.dongsan.domains.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.LineString;

import java.awt.geom.Point2D;

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
    private Integer time;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccessLevel accessLevel;

    @Column(nullable = false)
    private Point2D.Double startLocation;

    @Column(nullable = false)
    private Point2D.Double endLocation;

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

    // 연관관계 매핑도 생성 시 매핑합니다.
    @Builder
    private Walkway(String name, Double distance, Integer time, AccessLevel accessLevel, Point2D.Double startLocation,
                    Point2D.Double endLocation, String memo, LineString course, String courseImageUrl, Member member){
        this.name = name;
        this.distance = distance;
        this.time = time;
        this.accessLevel = accessLevel;
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
}
