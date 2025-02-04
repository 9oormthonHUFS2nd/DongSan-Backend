package fixture;

import com.dongsan.rdb.domains.member.MemberEntity;
import com.dongsan.rdb.domains.walkway.entity.LikedWalkway;
import com.dongsan.rdb.domains.walkway.entity.WalkwayEntity;

public class LikedWalkwayFixture {

    public static LikedWalkway createLikedWalkway(MemberEntity memberEntity, WalkwayEntity walkwayEntity) {
        return LikedWalkway.builder()
                .member(memberEntity)
                .walkway(walkwayEntity)
                .build();
    }

}
