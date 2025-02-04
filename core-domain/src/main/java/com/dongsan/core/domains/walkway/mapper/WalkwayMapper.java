package com.dongsan.core.domains.walkway.mapper;

import static com.dongsan.core.domains.walkway.mapper.LineStringMapper.toLineString;

import com.dongsan.domains.member.entity.Member;
import com.dongsan.core.domains.walkway.dto.request.CreateWalkwayRequest;
import com.dongsan.domains.walkway.entity.Walkway;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;

public class WalkwayMapper {
    private WalkwayMapper(){}

    public static Walkway toWalkway(CreateWalkwayRequest createWalkwayRequest, Member member, String courseImageUrl) {
        // 경로
        LineString course = toLineString(createWalkwayRequest.course());
        Point startLocation = course.getStartPoint();
        Point endLocation = course.getEndPoint();

        course.setSRID(4326);
        startLocation.setSRID(4326);
        endLocation.setSRID(4326);

        return Walkway.builder()
                .member(member)
                .name(createWalkwayRequest.name())
                .distance(createWalkwayRequest.distance())
                .time(createWalkwayRequest.time())
                .exposeLevel(createWalkwayRequest.exposeLevel())
                .startLocation(startLocation)
                .endLocation(endLocation)
                .memo(createWalkwayRequest.memo())
                .course(course)
                .courseImageUrl(courseImageUrl)
                .build();
    }
}
