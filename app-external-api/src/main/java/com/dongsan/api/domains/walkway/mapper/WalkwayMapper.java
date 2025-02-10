package com.dongsan.api.domains.walkway.mapper;

import com.dongsan.api.domains.walkway.dto.request.CreateWalkwayRequest;
import com.dongsan.api.domains.walkway.dto.request.UpdateWalkwayRequest;
import com.dongsan.core.domains.image.Image;
import com.dongsan.core.domains.walkway.domain.Author;
import com.dongsan.core.domains.walkway.domain.CourseInfo;
import com.dongsan.core.domains.walkway.domain.Walkway;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;

public class WalkwayMapper {
    private WalkwayMapper(){}

    public static Walkway toWalkway(CreateWalkwayRequest createWalkwayRequest, Image image, Long memberId) {

        LineString course = LineStringMapper.toLineString(createWalkwayRequest.course());
        Point startLocation = course.getStartPoint();
        Point endLocation = course.getEndPoint();

        course.setSRID(4326);
        startLocation.setSRID(4326);
        endLocation.setSRID(4326);

        CourseInfo courseInfo = CourseInfo.builder()
                .course(course)
                .courseImageUrl(image.url())
                .distance(createWalkwayRequest.distance())
                .time(createWalkwayRequest.time())
                .startLocation(startLocation)
                .endLocation(endLocation)
                .build();

        Author author = Author.builder()
                .authorId(memberId)
                .build();

        return Walkway.builder()
                .name(createWalkwayRequest.name())
                .exposeLevel(createWalkwayRequest.exposeLevel())
                .memo(createWalkwayRequest.memo())
                .author(author)
                .courseInfo(courseInfo)
                .build();
    }

    public static Walkway toWalkway(UpdateWalkwayRequest updateWalkwayRequest, Long walkwayId, Long memberId) {
        Author author = Author.builder()
                .authorId(memberId)
                .build();

        return Walkway.builder()
                .walkwayId(walkwayId)
                .name(updateWalkwayRequest.name())
                .exposeLevel(updateWalkwayRequest.exposeLevel())
                .memo(updateWalkwayRequest.memo())
                .author(author)
                .hashtags(updateWalkwayRequest.hashtags())
                .build();
    }
}
