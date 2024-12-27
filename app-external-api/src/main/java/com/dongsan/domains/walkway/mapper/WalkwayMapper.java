package com.dongsan.domains.walkway.mapper;

import static com.dongsan.domains.walkway.mapper.LineStringMapper.toLineString;

import com.dongsan.domains.member.entity.Member;
import com.dongsan.domains.walkway.dto.request.CreateWalkwayRequest;
import com.dongsan.domains.walkway.dto.response.CreateWalkwayResponse;
import com.dongsan.domains.walkway.entity.Walkway;
import com.dongsan.domains.walkway.enums.ExposeLevel;
import java.util.List;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;

public class WalkwayMapper {
    private WalkwayMapper(){}

    public static Walkway toWalkway(CreateWalkwayRequest createWalkwayRequest, Member member) {

        List<List<Double>> course = createWalkwayRequest.course();

        GeometryFactory geometryFactory = new GeometryFactory();

        // 시작점, 끝점
        List<Double> start = course.get(0);
        List<Double> end = course.get(course.size()-1);
        Point startLocation = geometryFactory.createPoint(new Coordinate(start.get(0), start.get(1)));
        Point endLocation = geometryFactory.createPoint(new Coordinate(end.get(0), end.get(1)));
        startLocation.setSRID(4326);
        endLocation.setSRID(4326);

        // 경로
        LineString courseResult = toLineString(course);
        courseResult.setSRID(4326);

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
