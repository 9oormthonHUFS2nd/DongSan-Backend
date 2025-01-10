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

    public static Walkway toWalkway(CreateWalkwayRequest createWalkwayRequest, Member member) {
        GeometryFactory geometryFactory = new GeometryFactory();

//        // 시작점, 끝점
//        List<Double> start = course.get(0);
//        List<Double> end = course.get(course.size()-1);
//        Point startLocation = geometryFactory.createPoint(new Coordinate(0.0, 0.0));
//        Point endLocation = geometryFactory.createPoint(new Coordinate(0.0, 0.0));
//        startLocation.setSRID(4326);
//        endLocation.setSRID(4326);

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
//                .exposeLevel(ExposeLevel.getExposeLevelByDescription(createWalkwayRequest.exposeLevel()))
                .startLocation(location)
                .endLocation(location)
                .memo(createWalkwayRequest.memo())
                .course(tempCourse)
//                .courseImageUrl()
                .build();
    }
}
