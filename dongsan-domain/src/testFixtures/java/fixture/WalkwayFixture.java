package fixture;

import com.dongsan.domains.common.entity.BaseEntity;
import com.dongsan.domains.member.entity.Member;
import com.dongsan.domains.walkway.entity.Walkway;
import com.dongsan.domains.walkway.enums.ExposeLevel;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;

public class WalkwayFixture {
    private static final String NAME = "Sample Walkway";
    private static final Double DISTANCE = 2.5;
    private static final Integer TIME = 30; // minutes
    private static final ExposeLevel EXPOSE_LEVEL = ExposeLevel.PUBLIC;
    private static final String MEMO = "A beautiful walkway.";
    private static final GeometryFactory GEOMETRY_FACTORY = new GeometryFactory();
    private static final Point START_LOCATION = GEOMETRY_FACTORY.createPoint(new Coordinate(0.0, 0.0));
    private static final Point END_LOCATION = GEOMETRY_FACTORY.createPoint(new Coordinate(1.0, 1.0));
    private static final LineString COURSE = GEOMETRY_FACTORY.createLineString(new Coordinate[]{
            new Coordinate(0.0, 0.0),
            new Coordinate(1.0, 1.0),
            new Coordinate(2.0, 2.0)
    });

    private static final String COURSE_IMAGE_URL = "http://example.com/course-image.png";

    public static Walkway createWalkway(Member member) {
        return Walkway.builder()
                .member(member)
                .name(NAME)
                .distance(DISTANCE)
                .time(TIME)
                .exposeLevel(EXPOSE_LEVEL)
                .startLocation(START_LOCATION)
                .endLocation(END_LOCATION)
                .memo(MEMO)
                .course(COURSE)
                .courseImageUrl(COURSE_IMAGE_URL)
                .build();
    }

    public static Walkway createWalkwayWithId(Long id, Member member){
        Walkway walkway = createWalkway(member);
        reflectId(id, walkway);
        reflectCreatedAt(LocalDateTime.now(), walkway);
        return walkway;
    }

    private static void reflectId(Long id, Walkway walkway){
        try {
            Field idField = Walkway.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(walkway, id);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private static void reflectCreatedAt(LocalDateTime createdAt, Walkway walkway){
        try {
            Field createdAtField = BaseEntity.class.getDeclaredField("createdAt");
            createdAtField.setAccessible(true);
            createdAtField.set(walkway, createdAt);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }


}
