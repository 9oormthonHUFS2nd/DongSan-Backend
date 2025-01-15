package fixture;

import com.dongsan.domains.common.entity.BaseEntity;
import com.dongsan.domains.image.entity.Image;
import java.lang.reflect.Field;
import java.time.LocalDateTime;

public class ImageFixture {
    private static final String URL = "https://test.com";

    public static Image createImage(String url) {
        return Image.builder()
                .url(url)
                .build();
    }

    public static Image createImage() {
        return Image.builder()
                .url(URL)
                .build();
    }

    public static Image createImageWithId(Long id, String url) {
        Image image = Image.builder()
                .url(url)
                .build();
        reflectId(id, image);
        reflectCreatedAt(LocalDateTime.now(), image);
        return image;
    }

    public static Image createImageWithId(Long id) {
        Image image = Image.builder()
                .url(URL)
                .build();
        reflectId(id, image);
        reflectCreatedAt(LocalDateTime.now(), image);
        return image;
    }

    private static void reflectId(Long id, Image image){
        try {
            Field idField = Image.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(image, id);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private static void reflectCreatedAt(LocalDateTime createdAt, Image image){
        try {
            Field createdAtField = BaseEntity.class.getDeclaredField("createdAt");
            createdAtField.setAccessible(true);
            createdAtField.set(image, createdAt);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
