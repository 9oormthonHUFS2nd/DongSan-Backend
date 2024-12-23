package fixture;

import com.dongsan.domains.bookmark.entity.Bookmark;
import com.dongsan.domains.common.entity.BaseEntity;
import com.dongsan.domains.member.entity.Member;

import java.lang.reflect.Field;
import java.time.LocalDateTime;

public class BookmarkFixture {

    private static final String NAME = "산책로 모음이에용";

    public static Bookmark createBookmark(Member member){
        return Bookmark.builder()
                .name(NAME)
                .member(member)
                .build();
    }

    public static Bookmark createBookmark(Member member, String name){
        return Bookmark.builder()
                .name(name)
                .member(member)
                .build();
    }

    public static Bookmark createBookmarkWithId(Long id, Member member){
        Bookmark bookmark = createBookmark(member);
        reflectId(id, bookmark);
        reflectCreatedAt(LocalDateTime.now(), bookmark);
        return bookmark;
    }

    public static Bookmark createBookmarkWithId(Long id, Member member, String name){
        Bookmark bookmark = createBookmark(member, name);
        reflectId(id, bookmark);
        reflectCreatedAt(LocalDateTime.now(), bookmark);
        return bookmark;
    }

    private static void reflectId(Long id, Bookmark bookmark){
        try {
            Field idField = Bookmark.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(bookmark, id);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private static void reflectCreatedAt(LocalDateTime createdAt, Bookmark bookmark){
        try {
            Field createdAtField = BaseEntity.class.getDeclaredField("createdAt");
            createdAtField.setAccessible(true);
            createdAtField.set(bookmark, createdAt);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
