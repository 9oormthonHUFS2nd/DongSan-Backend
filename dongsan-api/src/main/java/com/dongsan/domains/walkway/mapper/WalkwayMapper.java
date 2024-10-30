package com.dongsan.domains.walkway.mapper;

import static com.dongsan.domains.walkway.mapper.LineStringMapper.toLineString;
import static com.dongsan.domains.walkway.mapper.LineStringMapper.toList;

import com.dongsan.domains.hashtag.entity.Hashtag;
import com.dongsan.domains.member.entity.Member;
import com.dongsan.domains.walkway.dto.request.CreateWalkwayRequest;
import com.dongsan.domains.walkway.dto.response.CreateWalkwayResponse;
import com.dongsan.domains.walkway.dto.response.GetWalkwayWithLikedResponse;
import com.dongsan.domains.walkway.entity.Walkway;
import com.dongsan.domains.walkway.enums.ExposeLevel;
import java.awt.geom.Point2D;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.locationtech.jts.geom.LineString;

public class WalkwayMapper {

    public static Walkway toWalkway(CreateWalkwayRequest createWalkwayRequest, Member member) {

        List<List<Double>> course = createWalkwayRequest.course();

        // 시작점, 끝점
        List<Double> start = course.get(0);
        List<Double> end = course.get(course.size()-1);
        Point2D.Double startLocation = new Point2D.Double(start.get(0), start.get(1));
        Point2D.Double endLocation = new Point2D.Double(end.get(0), end.get(1));

        // 경로
        LineString courseResult = toLineString(course);

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

    public static GetWalkwayWithLikedResponse toGetWalkwayWithLikedResponse(
            Walkway walkway,
            Boolean isLikedWalkway,
            List<Hashtag> hashtags
    ) {

        return GetWalkwayWithLikedResponse.builder()
                .date(walkway.getCreatedAt().toLocalDate().toString())
                .time(walkway.getTime().toString())
                .distance(walkway.getDistance())
                .name(walkway.getName())
                .memo(walkway.getMemo())
                .rating(walkway.getRating())
                .isLiked(!Objects.isNull(isLikedWalkway))
                .reviewCount(walkway.getReviewCount())
                .hashTags(hashtags.stream()
                        .map(Hashtag::getName).
                        collect(Collectors.toList()))
                .accessLevel(walkway.getExposeLevel().toBoolean())
                .course(toList(walkway.getCourse()))
                .build();
    }
}
