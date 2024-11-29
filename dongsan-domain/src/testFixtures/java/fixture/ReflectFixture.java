package fixture;

import com.dongsan.domains.common.entity.BaseEntity;
import com.dongsan.domains.member.entity.Member;
import java.lang.reflect.Field;
import java.time.LocalDateTime;

public class ReflectFixture {

    public static void reflectField(Object object, String field, Object value){
        try{
            Field reflectField = object.getClass().getDeclaredField(field);
            reflectField.setAccessible(true);
            reflectField.set(object, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static void reflectCreatedAt(LocalDateTime createdAt, Object object){
        try {
            Field createdAtField = BaseEntity.class.getDeclaredField("createdAt");
            createdAtField.setAccessible(true);
            createdAtField.set(object, createdAt);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}