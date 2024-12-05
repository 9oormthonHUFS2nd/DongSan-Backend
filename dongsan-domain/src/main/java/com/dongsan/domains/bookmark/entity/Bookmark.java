package com.dongsan.domains.bookmark.entity;

import com.dongsan.domains.common.entity.BaseEntity;
import com.dongsan.domains.member.entity.Member;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Bookmark extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;  // 북마크 생성자

    @Column(nullable = false)
    private String name;

    // 엔티티 생명 주기 (REMOVE)
    @OneToMany(mappedBy = "bookmark", cascade = CascadeType.REMOVE)
    List<MarkedWalkway> markedWalkways = new ArrayList<>();

    @Builder
    private Bookmark(String name, Member member){
        this.name = name;

        // 연관관계 매핑
        this.member= member;
    }

    public void addMarkedWalkway(MarkedWalkway markedWalkway){
        this.markedWalkways.add(markedWalkway);
    }

    public void rename(String name){
        this.name = name;
    }
}
