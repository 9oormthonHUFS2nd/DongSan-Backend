package fixture;

import com.dongsan.domains.member.entity.Member;
import com.dongsan.domains.review.entity.Review;
import com.dongsan.domains.walkway.entity.Walkway;
import java.lang.reflect.Field;

public class ReviewFixture {
    private static final Byte RATING = 5;
    private static final String CONTENT = "리뷰 내용";

    public static Review createReview(Member member, Walkway walkway){
        return Review.builder()
                .member(member)
                .walkway(walkway)
                .rating(RATING)
                .content(CONTENT)
                .build();
    }

    public static Review createReview(Member member, Walkway walkway, Byte rating, String content){
        return Review.builder()
                .member(member)
                .walkway(walkway)
                .rating(rating)
                .content(content)
                .build();
    }

    public static Review createReviewWithId(Long id, Member member, Walkway walkway){
        Review review = createReview(member, walkway);

        try {
            Field idField = Review.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(review, id);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return review;
    }

    public static Review createReviewWithId(Long id, Member member, Walkway walkway, Byte rating, String content){
        Review review = createReview(member, walkway, rating, content);

        try {
            Field idField = Review.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(review, id);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return review;
    }


}
