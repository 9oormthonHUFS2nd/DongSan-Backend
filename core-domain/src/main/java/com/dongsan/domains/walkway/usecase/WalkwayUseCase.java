package com.dongsan.domains.walkway.usecase;

import com.dongsan.common.annotation.UseCase;
import com.dongsan.common.error.code.WalkwayErrorCode;
import com.dongsan.common.error.exception.CustomException;
import com.dongsan.domains.image.service.ImageQueryService;
import com.dongsan.domains.member.entity.Member;
import com.dongsan.domains.user.service.MemberQueryService;
import com.dongsan.domains.walkway.dto.request.CreateWalkwayRequest;
import com.dongsan.domains.walkway.dto.request.SearchWalkwayQuery;
import com.dongsan.domains.walkway.dto.request.UpdateWalkwayRequest;
import com.dongsan.domains.walkway.dto.response.SearchWalkwayResult;
import com.dongsan.domains.walkway.entity.Walkway;
import com.dongsan.domains.walkway.enums.Sort;
import com.dongsan.domains.walkway.mapper.WalkwayMapper;
import com.dongsan.domains.walkway.service.HashtagWalkwayCommandService;
import com.dongsan.domains.walkway.service.WalkwayCommandService;
import com.dongsan.domains.walkway.service.WalkwayQueryService;
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

    @Transactional(readOnly = true)
    public Walkway getWalkwayWithLiked(Long walkwayId, Long memberId) {
        Walkway walkway = walkwayQueryService.getWalkwayWithHashtagAndLike(memberId, walkwayId);
        if (walkway == null) {
            throw new CustomException(WalkwayErrorCode.INVALID_COURSE);
        }

        return walkway;
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

    @Transactional(readOnly = true)
    public List<SearchWalkwayResult> searchWalkway(
            Long userId,
            String sortType,
            Double latitude,
            Double longitude,
            Double distance,
            Long lastId,
            int size
    ) {
        Walkway lastWalkway = null;

        if (lastId != null) {
            lastWalkway = walkwayQueryService.getWalkway(lastId);
        }

        Sort sort = Sort.typeOf(sortType);

        SearchWalkwayQuery searchWalkwayRequest
                = new SearchWalkwayQuery(userId, longitude, latitude, distance, lastWalkway, size);

        return walkwayQueryService.searchWalkway(searchWalkwayRequest, sort);
    }
}
