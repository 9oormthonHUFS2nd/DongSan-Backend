package com.dongsan.core.domains.walkway.usecase;

import com.dongsan.core.domains.member.MemberReader;
import com.dongsan.core.domains.walkway.service.HashtagWalkwayWriter;
import com.dongsan.core.domains.walkway.service.WalkwayWriter;
import com.dongsan.core.domains.walkway.service.WalkwayReader;
import com.dongsan.core.common.annotation.UseCase;
import com.dongsan.common.error.code.WalkwayErrorCode;
import com.dongsan.common.error.exception.CustomException;
import com.dongsan.core.domains.image.ImageReader;
import com.dongsan.domains.member.entity.Member;
import com.dongsan.core.domains.walkway.dto.request.CreateWalkwayRequest;
import com.dongsan.domains.walkway.dto.request.SearchWalkwayQuery;
import com.dongsan.core.domains.walkway.dto.request.UpdateWalkwayRequest;
import com.dongsan.domains.walkway.dto.response.SearchWalkwayResult;
import com.dongsan.domains.walkway.entity.Walkway;
import com.dongsan.core.domains.walkway.enums.WalkwaySort;
import com.dongsan.core.domains.walkway.mapper.WalkwayMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@RequiredArgsConstructor
public class WalkwayService {

    private final WalkwayWriter walkwayCommandService;
    private final WalkwayReader walkwayQueryService;
    private final MemberReader memberReader;
    private final HashtagWalkwayWriter hashtagWalkwayWriter;
    private final HashtagService hashtagUseCase;
    private final ImageReader imageReader;

    @Transactional
    public Walkway createWalkway(CreateWalkwayRequest createWalkwayRequest, Long memberId) {
        Member member = memberReader.getMember(memberId);
        String courseImageUrl = imageReader.getImage(createWalkwayRequest.courseImageId()).getUrl();
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

    public boolean isMarkedWalkway(Long walkwayId, Long memberId) {
        return walkwayQueryService.isMarkedWalkway(walkwayId, memberId);
    }

    @Transactional
    public void updateWalkway(UpdateWalkwayRequest updateWalkwayRequest, Long memberId, Long walkwayId) {
        // 산책로 불러오기
        walkwayQueryService.isOwnerOfWalkway(walkwayId, memberId);
        Walkway walkway = walkwayQueryService.getWalkway(walkwayId);

        // 해쉬 태그 추가 및 삭제
        hashtagWalkwayWriter.deleteAllHashtagWalkways(walkway);
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

        WalkwaySort sort = WalkwaySort.typeOf(sortType);

        SearchWalkwayQuery searchWalkwayRequest
                = new SearchWalkwayQuery(userId, longitude, latitude, distance, lastWalkway, size);

        return walkwayQueryService.searchWalkway(searchWalkwayRequest, sort);
    }
}
