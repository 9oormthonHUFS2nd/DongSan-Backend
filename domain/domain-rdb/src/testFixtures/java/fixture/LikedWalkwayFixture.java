package fixture;

import com.dongsan.domains.member.entity.Member;
import com.dongsan.domains.walkway.entity.LikedWalkway;
import com.dongsan.domains.walkway.entity.Walkway;

public class LikedWalkwayFixture {

    public static LikedWalkway createLikedWalkway(Member member, Walkway walkway) {
        return LikedWalkway.builder()
                .member(member)
                .walkway(walkway)
                .build();
    }

}
