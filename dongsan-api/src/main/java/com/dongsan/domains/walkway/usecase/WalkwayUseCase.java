package com.dongsan.domains.walkway.usecase;

import com.dongsan.common.annotation.UseCase;
import com.dongsan.domains.hashtag.service.HashtagCommandService;
import com.dongsan.domains.hashtag.service.HashtagQueryService;
import com.dongsan.domains.hashtag.service.HashtagWalkwayCommandService;
import com.dongsan.domains.member.entity.Member;
import com.dongsan.domains.member.service.MemberQueryService;
import com.dongsan.domains.walkway.dto.SearchWalkwayPopular;
import com.dongsan.domains.walkway.dto.SearchWalkwayRating;
import com.dongsan.domains.walkway.dto.request.CreateWalkwayRequest;
import com.dongsan.domains.walkway.dto.request.UpdateWalkwayRequest;
import com.dongsan.domains.walkway.dto.response.CreateWalkwayResponse;
import com.dongsan.domains.walkway.dto.response.GetWalkwaySearchResponse;
import com.dongsan.domains.walkway.dto.response.GetWalkwayWithLikedResponse;
import com.dongsan.domains.walkway.entity.Walkway;
import com.dongsan.domains.walkway.mapper.WalkwayMapper;
import com.dongsan.domains.walkway.service.WalkwayCommandService;
import com.dongsan.domains.walkway.service.WalkwayQueryService;
import com.dongsan.error.code.WalkwayErrorCode;
import com.dongsan.error.exception.CustomException;
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
    private final HashtagQueryService hashtagQueryService;
    private final HashtagCommandService hashtagCommandService;
    private final HashtagUseCase hashtagUseCase;

    @Transactional
    public CreateWalkwayResponse createWalkway(CreateWalkwayRequest createWalkwayRequest, Long memberId) {

        Member member = memberQueryService.getMember(memberId);

        Walkway walkway = walkwayCommandService.createWalkway(WalkwayMapper.toWalkway(createWalkwayRequest, member));

        // 해쉬태그 추가
        if (!createWalkwayRequest.hashtags().isEmpty()) {
            hashtagUseCase.createHashtagWalkways(walkway, createWalkwayRequest.hashtags());
        }

        // TODO: 경로 이미지 파일 저장

        return WalkwayMapper.toCreateWalkwayResponse(walkway);
    }

    @Transactional(readOnly = true)
    public GetWalkwayWithLikedResponse getWalkwayWithLiked(Long walkwayId, Long memberId) {
        Walkway walkway = walkwayQueryService.getWalkwayWithHashtagAndLike(memberId, walkwayId);
        if (walkway == null) {
            throw new CustomException(WalkwayErrorCode.INVALID_COURSE);
        }

        return WalkwayMapper.toGetWalkwayWithLikedResponse(walkway);
    }

    @Transactional(readOnly = true)
    public GetWalkwaySearchResponse getWalkwaysSearch(
            Long userId,
            String type,
            Double latitude,
            Double longitude,
            Double distance,
            String hashtags,
            Long lastId,
            Double lastRating,
            Integer lastLikes,
            int size
    ) {
        List<String> hashtagsList = List.of(hashtags.split(","));

        int distanceInt = (int) (distance * 1000);

        List<Walkway> walkways = switch (type) {
            case "liked" -> walkwayQueryService.getWalkwaysPopular(
                    new SearchWalkwayPopular(userId, longitude, latitude, distanceInt, hashtagsList, lastId, lastLikes,
                            size)
            );
            case "rating" -> walkwayQueryService.getWalkwaysRating(
                    new SearchWalkwayRating(userId, longitude, latitude, distanceInt, hashtagsList, lastId, lastRating,
                            size)
            );
            default -> throw new CustomException(WalkwayErrorCode.INVALID_SEARCH_TYPE);
        };

        return WalkwayMapper.toGetWalkwaySearchResponse(walkways, size);
    }

    @Transactional
    public void updateWalkway(UpdateWalkwayRequest updateWalkwayRequest, Long memberId, Long walkwayId) {
        Member member = memberQueryService.getMember(memberId);

        // 산책로 불러오기
        Walkway walkway = walkwayQueryService.getWalkwayWithHashtag(walkwayId);

        if (!walkway.getMember().equals(member)) {
            throw new CustomException(WalkwayErrorCode.NOT_WALKWAY_OWNER);
        }

        // 해쉬 태그 추가 및 삭제
        hashtagWalkwayCommandService.deleteAllHashtagWalkways(walkway);
        walkway.removeAllHashtagWalkway();
        if (!updateWalkwayRequest.hashtags().isEmpty()) {
            hashtagUseCase.createHashtagWalkways(walkway, updateWalkwayRequest.hashtags());
        }

        // 산책로 수정
        walkway.updateWalkway(updateWalkwayRequest.name(), updateWalkwayRequest.memo(),
                updateWalkwayRequest.exposeLevel());

        walkwayCommandService.createWalkway(walkway);
    }
}
