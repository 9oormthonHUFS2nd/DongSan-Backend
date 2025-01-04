package fixture;

import com.dongsan.domains.common.entity.BaseEntity;
import com.dongsan.domains.member.entity.Member;

import java.lang.reflect.Field;
import java.time.LocalDateTime;

public class MemberFixture {
    private static final String EMAIL = "abc@gmail.com";
    private static final String NICKNAME = "동네산책";
    private static final String PROFILE_IMAGE_URL = "image.png";

    public static Member createMember(){
        return Member.builder()
                .email(EMAIL)
                .nickname(NICKNAME)
                .profileImageUrl(PROFILE_IMAGE_URL)
                .build();
    }

    public static Member createMember(String email, String nickname, String profileImageUrl){
        return Member.builder()
                .email(email)
                .nickname(nickname)
                .profileImageUrl(profileImageUrl)
                .build();
    }

    public static Member createMemberWithId(Long id){
        Member member = createMember();
        reflectId(id, member);
        reflectCreatedAt(LocalDateTime.now(), member);
        return member;
    }

    public static Member createMemberWithId(Long id, String email, String nickname, String profileImageUrl){
        Member member = createMember(email, nickname, profileImageUrl);
        reflectId(id, member);
        reflectCreatedAt(LocalDateTime.now(), member);
        return member;
    }

    private static void reflectId(Long id, Member member){
        try {
            Field idField = Member.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(member, id);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private static void reflectCreatedAt(LocalDateTime createdAt, Member member){
        try {
            Field createdAtField = BaseEntity.class.getDeclaredField("createdAt");
            createdAtField.setAccessible(true);
            createdAtField.set(member, createdAt);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}
