package fixture;

import com.dongsan.domains.bookmark.entity.Bookmark;
import com.dongsan.domains.bookmark.entity.MarkedWalkway;
import com.dongsan.domains.walkway.entity.Walkway;

public class MarkedWalkwayFixture {
    public static MarkedWalkway createMarkedWalkway(Bookmark bookmark, Walkway walkway){
        return MarkedWalkway.builder()
                .bookmark(bookmark)
                .walkway(walkway)
                .build();
    }

}
