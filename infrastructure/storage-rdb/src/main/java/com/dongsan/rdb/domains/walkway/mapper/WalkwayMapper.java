package com.dongsan.rdb.domains.walkway.mapper;

import com.dongsan.core.domains.walkway.domain.Author;
import com.dongsan.core.domains.walkway.domain.CourseInfo;
import com.dongsan.core.domains.walkway.domain.Walkway;
import com.dongsan.rdb.domains.member.MemberEntity;
import com.dongsan.rdb.domains.walkway.entity.WalkwayEntity;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;

public class WalkwayMapper {
    private WalkwayMapper(){}

    public static WalkwayEntity toWalkwayEntity(Walkway walkway, MemberEntity memberEntity) {
        // 경로
        LineString course = walkway.courseInfo().course();
        Point startLocation = walkway.courseInfo().startLocation();
        Point endLocation = walkway.courseInfo().endLocation();

        course.setSRID(4326);
        startLocation.setSRID(4326);
        endLocation.setSRID(4326);

        return WalkwayEntity.builder()
                .name(walkway.name())
                .distance(walkway.courseInfo().distance())
                .time(walkway.courseInfo().time())
                .exposeLevel(walkway.exposeLevel())
                .startLocation(startLocation)
                .endLocation(endLocation)
                .memo(walkway.memo())
                .course(course)
                .courseImageUrl(walkway.courseInfo().courseImageUrl())
                .memberEntity(memberEntity)
                .hashtags(walkway.hashtags())
                .build();
    }

    public static Walkway toWalkway(WalkwayEntity walkwayEntity) {
        CourseInfo courseInfo = CourseInfo.builder()
                .course(walkwayEntity.getCourse())
                .courseImageUrl(walkwayEntity.getCourseImageUrl())
                .distance(walkwayEntity.getDistance())
                .time(walkwayEntity.getTime())
                .startLocation(walkwayEntity.getStartLocation())
                .endLocation(walkwayEntity.getEndLocation())
                .build();

        Author author = Author.builder()
                .authorId(walkwayEntity.getMemberEntity().getId())
                .build();

        return Walkway.builder()
                .walkwayId(walkwayEntity.getId())
                .name(walkwayEntity.getName())
                .exposeLevel(walkwayEntity.getExposeLevel())
                .memo(walkwayEntity.getMemo())
                .author(author)
                .courseInfo(courseInfo)
                .createdAt(walkwayEntity.getCreatedAt())
                .build();
    }
}
