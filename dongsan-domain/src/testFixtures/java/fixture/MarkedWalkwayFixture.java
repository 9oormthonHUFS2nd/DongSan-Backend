package fixture;

import com.dongsan.domains.bookmark.entity.Bookmark;
import com.dongsan.domains.bookmark.entity.MarkedWalkway;
import com.dongsan.domains.common.entity.BaseEntity;
import com.dongsan.domains.walkway.entity.Walkway;
import java.lang.reflect.Field;
import java.time.LocalDateTime;

public class MarkedWalkwayFixture {

    public static MarkedWalkway createMarkedWalkway(Walkway walkway, Bookmark bookmark){
        return MarkedWalkway.builder()
                .walkway(walkway)
                .bookmark(bookmark)
                .build();
    }


    public static MarkedWalkway createMarkedWalkwayWithId(Walkway walkway, Bookmark bookmark, Long id){
        MarkedWalkway markedWalkway = createMarkedWalkway(walkway, bookmark);
        reflectId(id, markedWalkway);
        reflectCreatedAt(LocalDateTime.now(), markedWalkway);
        return markedWalkway;
    }


    private static void reflectId(Long id, MarkedWalkway markedWalkway){
        try {
            Field idField = MarkedWalkway.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(markedWalkway, id);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private static void reflectCreatedAt(LocalDateTime createdAt, MarkedWalkway markedWalkway){
        try {
            Field createdAtField = BaseEntity.class.getDeclaredField("createdAt");
            createdAtField.setAccessible(true);
            createdAtField.set(markedWalkway, createdAt);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}
