package com.dongsan.domains.walkway.mapper;

import static com.dongsan.domains.walkway.mapper.LineStringMapper.toLineString;

import com.dongsan.domains.member.entity.Member;
import com.dongsan.domains.walkway.dto.request.CreateWalkwayRequest;
import com.dongsan.domains.walkway.entity.Walkway;
import java.util.ArrayList;
import java.util.List;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;

public class WalkwayMapper {
    private WalkwayMapper(){}

    public static Walkway toWalkway(CreateWalkwayRequest createWalkwayRequest, Member member, String courseImageUrl) {
        GeometryFactory geometryFactory = new GeometryFactory();

        // 경로
        List<List<Double>> course = new ArrayList<>();
        course.add(List.of(0.0, 0.0));
        course.add(List.of(0.0, 0.0));
        LineString tempCourse = toLineString(course);
        tempCourse.setSRID(4326);

        Point location = geometryFactory.createPoint(new Coordinate(0.0, 0.0));
        location.setSRID(4326);

        return Walkway.builder()
                .member(member)
                .name(createWalkwayRequest.name())
                .distance(createWalkwayRequest.distance())
                .time(createWalkwayRequest.time())
                .exposeLevel(createWalkwayRequest.exposeLevel())
                .startLocation(location)
                .endLocation(location)
                .memo(createWalkwayRequest.memo())
                .course(tempCourse)
                .courseImageUrl(courseImageUrl)
                .build();
    }
}
