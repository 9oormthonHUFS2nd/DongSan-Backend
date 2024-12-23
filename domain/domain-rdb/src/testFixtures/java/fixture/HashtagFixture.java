package fixture;

import com.dongsan.domains.common.entity.BaseEntity;
import com.dongsan.domains.hashtag.entity.Hashtag;

import java.lang.reflect.Field;
import java.time.LocalDateTime;

public class HashtagFixture {

    private static final String NAME = "산책로 태그";

    public static Hashtag createHashtag(){
        return Hashtag.builder()
                .name(NAME)
                .build();
    }

    public static Hashtag createHashtag(String name){
        return Hashtag.builder()
                .name(name)
                .build();
    }

    public static Hashtag createHashtagWithId(Long id){
        Hashtag hashtag = createHashtag();
        reflectId(id, hashtag);
        reflectCreatedAt(LocalDateTime.now(), hashtag);
        return hashtag;
    }

    public static Hashtag createHashtagWithId(Long id, String name){
        Hashtag hashtag = createHashtag(name);
        reflectId(id, hashtag);
        reflectCreatedAt(LocalDateTime.now(), hashtag);
        return hashtag;
    }

    private static void reflectId(Long id, Hashtag hashtag){
        try {
            Field idField = Hashtag.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(hashtag, id);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private static void reflectCreatedAt(LocalDateTime createdAt, Hashtag hashtag){
        try {
            Field createdAtField = BaseEntity.class.getDeclaredField("createdAt");
            createdAtField.setAccessible(true);
            createdAtField.set(hashtag, createdAt);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
