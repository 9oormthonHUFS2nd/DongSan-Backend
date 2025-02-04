package fixture;

import com.dongsan.rdb.domains.bookmark.BookmarkEntity;
import com.dongsan.rdb.domains.bookmark.MarkedWalkway;
import com.dongsan.rdb.domains.common.entity.BaseEntity;
import com.dongsan.rdb.domains.walkway.entity.WalkwayEntity;
import java.lang.reflect.Field;
import java.time.LocalDateTime;

public class MarkedWalkwayFixture {

    public static MarkedWalkway createMarkedWalkway(WalkwayEntity walkwayEntity, BookmarkEntity bookmarkEntity){
        return MarkedWalkway.builder()
                .walkway(walkwayEntity)
                .bookmark(bookmarkEntity)
                .build();
    }


    public static MarkedWalkway createMarkedWalkwayWithId(WalkwayEntity walkwayEntity, BookmarkEntity bookmarkEntity, Long id){
        MarkedWalkway markedWalkway = createMarkedWalkway(walkwayEntity, bookmarkEntity);
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
