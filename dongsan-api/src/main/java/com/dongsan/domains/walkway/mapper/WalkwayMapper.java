package com.dongsan.domains.walkway.mapper;

import com.dongsan.domains.member.entity.Member;
import com.dongsan.domains.walkway.dto.request.CreateWalkwayRequest;
import com.dongsan.domains.walkway.dto.response.CreateWalkwayResponse;
import com.dongsan.domains.walkway.entity.Walkway;
import com.dongsan.domains.walkway.enums.ExposeLevel;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;

import java.awt.geom.Point2D;
import java.util.List;

public class WalkwayMapper {

    public static Walkway toWalkway(CreateWalkwayRequest createWalkwayRequest, Member member) {

        List<List<Double>> course = createWalkwayRequest.course();

        // 시작점, 끝점
        List<Double> start = course.get(0);
        List<Double> end = course.get(course.size()-1);
        Point2D.Double startLocation = new Point2D.Double(start.get(0), start.get(1));
        Point2D.Double endLocation = new Point2D.Double(end.get(0), end.get(1));

        // 경로
        GeometryFactory geometryFactory = new GeometryFactory();
        Coordinate[] coordinateList = new Coordinate[course.size()];

        for(int i = 0; i < course.size(); i++) {
            List<Double> point = course.get(i);
            coordinateList[i] = new Coordinate(point.get(0), point.get(1));
        }

        LineString courseResult = geometryFactory.createLineString(coordinateList);

        return Walkway.builder()
                .member(member)
                .name(createWalkwayRequest.name())
                .distance(createWalkwayRequest.distance())
                .time(createWalkwayRequest.time())
                .exposeLevel(ExposeLevel.getExposeLevelByDescription(createWalkwayRequest.exposeLevel()))
                .startLocation(startLocation)
                .endLocation(endLocation)
                .memo(createWalkwayRequest.memo())
                .course(courseResult)
//                .courseImageUrl()
                .build();
    }

    public static CreateWalkwayResponse toCreateWalkwayResponse(Walkway walkway) {
        return CreateWalkwayResponse.builder()
                .walkwayId(walkway.getId())
                .build();
    }
}
