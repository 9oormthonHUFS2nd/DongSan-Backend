package fixture;

import com.dongsan.domains.common.entity.BaseEntity;
import com.dongsan.domains.member.entity.Member;
import com.dongsan.domains.review.entity.Review;
import com.dongsan.domains.walkway.entity.Walkway;

import java.lang.reflect.Field;
import java.time.LocalDateTime;

public class ReviewFixture {
    private static final Integer RATING = 5;
    private static final String CONTENT = "리뷰 내용";

    public static Review createReview(Member member, Walkway walkway){
        return Review.builder()
                .member(member)
                .walkway(walkway)
                .rating(RATING)
                .content(CONTENT)
                .build();

    }

    public static Review createReview(Member member, Walkway walkway, Integer rating, String content){
        return Review.builder()
                .member(member)
                .walkway(walkway)
                .rating(rating)
                .content(content)
                .build();
    }

    public static Review createReviewWithId(Long id, Member member, Walkway walkway){
        Review review = createReview(member, walkway);
        reflectId(id, review);
        reflectCreatedAt(LocalDateTime.now(), review);
        return review;
    }

    public static Review createReviewWithId(Long id, Member member, Walkway walkway, Integer rating, String content){
        Review review = createReview(member, walkway, rating, content);
        reflectId(id, review);
        reflectCreatedAt(LocalDateTime.now(), review);
        return review;
    }

    private static void reflectId(Long id, Review review){
        try {
            Field idField = Review.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(review, id);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private static void reflectCreatedAt(LocalDateTime createdAt, Review review){
        try {
            Field createdAtField = BaseEntity.class.getDeclaredField("createdAt");
            createdAtField.setAccessible(true);
            createdAtField.set(review, createdAt);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }


}
