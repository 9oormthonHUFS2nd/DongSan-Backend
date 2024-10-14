package fixture;

import com.dongsan.domains.common.entity.BaseEntity;
import com.dongsan.domains.hashtag.entity.Hashtag;
import com.dongsan.domains.hashtag.entity.HashtagWalkway;
import com.dongsan.domains.walkway.entity.Walkway;

import java.lang.reflect.Field;
import java.time.LocalDateTime;

public class HashtagWalkwayFixture {

    private static final String NAME = "산책로 태그";

    public static HashtagWalkway createHashtagWalkway(Walkway walkway, Hashtag hashtag){
        return HashtagWalkway.builder()
                .walkway(walkway)
                .hashtag(hashtag)
                .build();
    }


    public static HashtagWalkway createHashtagWalkwayWithId(Walkway walkway, Hashtag hashtag, Long id){
        HashtagWalkway hashtagWalkway = createHashtagWalkway(walkway, hashtag);
        reflectId(id, hashtagWalkway);
        reflectCreatedAt(LocalDateTime.now(), hashtagWalkway);
        return hashtagWalkway;
    }


    private static void reflectId(Long id, HashtagWalkway hashtagWalkway){
        try {
            Field idField = HashtagWalkway.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(hashtagWalkway, id);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private static void reflectCreatedAt(LocalDateTime createdAt, HashtagWalkway hashtagWalkway){
        try {
            Field createdAtField = BaseEntity.class.getDeclaredField("createdAt");
            createdAtField.setAccessible(true);
            createdAtField.set(hashtagWalkway, createdAt);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
