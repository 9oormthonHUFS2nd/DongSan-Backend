package com.dongsan.domains.walkway.usecase;

import com.dongsan.common.annotation.UseCase;
import com.dongsan.common.error.code.WalkwayErrorCode;
import com.dongsan.common.error.exception.CustomException;
import com.dongsan.domains.image.service.ImageQueryService;
import com.dongsan.domains.member.entity.Member;
import com.dongsan.domains.user.service.MemberQueryService;
import com.dongsan.domains.walkway.dto.SearchWalkwayPopular;
import com.dongsan.domains.walkway.dto.SearchWalkwayRating;
import com.dongsan.domains.walkway.dto.request.CreateWalkwayCourseRequest;
import com.dongsan.domains.walkway.dto.request.CreateWalkwayRequest;
import com.dongsan.domains.walkway.dto.request.UpdateWalkwayRequest;
import com.dongsan.domains.walkway.entity.Walkway;
import com.dongsan.domains.walkway.mapper.LineStringMapper;
import com.dongsan.domains.walkway.mapper.WalkwayMapper;
import com.dongsan.domains.walkway.service.HashtagWalkwayCommandService;
import com.dongsan.domains.walkway.service.WalkwayCommandService;
import com.dongsan.domains.walkway.service.WalkwayQueryService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@RequiredArgsConstructor
public class WalkwayUseCase {

    private final WalkwayCommandService walkwayCommandService;
    private final WalkwayQueryService walkwayQueryService;
    private final MemberQueryService memberQueryService;
    private final HashtagWalkwayCommandService hashtagWalkwayCommandService;
    private final HashtagUseCase hashtagUseCase;
    private final ImageQueryService imageQueryService;

    @Transactional
    public Walkway createWalkway(CreateWalkwayRequest createWalkwayRequest, Long memberId) {
        Member member = memberQueryService.getMember(memberId);
        String courseImageUrl = imageQueryService.getImage(createWalkwayRequest.courseImageId()).getUrl();
        return walkwayCommandService.createWalkway(WalkwayMapper.toWalkway(createWalkwayRequest, member, courseImageUrl));
    }

    public Walkway createWalkwayCourse(CreateWalkwayCourseRequest createWalkwayCourseRequest, Long memberId, Long walkwayId) {
        walkwayQueryService.isOwnerOfWalkway(walkwayId, memberId);
        Walkway walkway = walkwayQueryService.getWalkway(walkwayId);
        try {
            walkway.registerCourse(LineStringMapper.toLineString(createWalkwayCourseRequest.course()));
            return walkwayCommandService.createWalkway(walkway);
        } catch (RuntimeException e) {
            hashtagWalkwayCommandService.deleteAllHashtagWalkways(walkway);
            walkwayCommandService.deleteWalkway(walkway);
            throw new CustomException(WalkwayErrorCode.INVALID_COURSE);
        }
    }

    @Transactional(readOnly = true)
    public Walkway getWalkwayWithLiked(Long walkwayId, Long memberId) {
        Walkway walkway = walkwayQueryService.getWalkwayWithHashtagAndLike(memberId, walkwayId);
        if (walkway == null) {
            throw new CustomException(WalkwayErrorCode.INVALID_COURSE);
        }

        return walkway;
    }

    @Transactional(readOnly = true)
    public List<Walkway> getWalkwaysSearch(
            Long userId,
            String type,
            Double latitude,
            Double longitude,
            Double distance,
            String hashtags,
            Long lastId,
            int size
    ) {
        List<String> hashtagsList = new ArrayList<>();

        if (!hashtags.isBlank()) {
            Arrays.stream(hashtags.split(",")).forEach(hashtag -> hashtagsList.add(hashtag.trim()));
        }

        int distanceInt = (int) (distance * 1000);

        Walkway lastWalkway = null;

        if (lastId != null) {
            lastWalkway = walkwayQueryService.getWalkway(lastId);
        }

        List<Walkway> walkways = switch (type) {
            case "liked" -> walkwayQueryService.getWalkwaysPopular(
                    new SearchWalkwayPopular(userId, longitude, latitude, distanceInt, hashtagsList, lastWalkway, size)
            );
            case "rating" -> walkwayQueryService.getWalkwaysRating(
                    new SearchWalkwayRating(userId, longitude, latitude, distanceInt, hashtagsList, lastWalkway, size)
            );
            default -> throw new CustomException(WalkwayErrorCode.INVALID_SEARCH_TYPE);
        };

        return walkways;
    }

    @Transactional
    public void updateWalkway(UpdateWalkwayRequest updateWalkwayRequest, Long memberId, Long walkwayId) {
        // 산책로 불러오기
        walkwayQueryService.isOwnerOfWalkway(walkwayId, memberId);
        Walkway walkway = walkwayQueryService.getWalkway(walkwayId);

        // 해쉬 태그 추가 및 삭제
        hashtagWalkwayCommandService.deleteAllHashtagWalkways(walkway);
        walkway.removeAllHashtagWalkway();
        hashtagUseCase.createHashtagWalkways(walkway, updateWalkwayRequest.hashtags());

        // 산책로 수정
        walkway.updateWalkway(updateWalkwayRequest.name(), updateWalkwayRequest.memo(), updateWalkwayRequest.exposeLevel());
        walkwayCommandService.createWalkway(walkway);
    }
}
