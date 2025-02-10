package com.dongsan.core.domains.image;

import com.dongsan.core.common.annotation.UseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@RequiredArgsConstructor
public class ImageService {
    private final ImageWriter imageWriter;
    private final ImageReader imageReader;

    @Transactional
    public Image createImage(String url) {
        return imageWriter.createImage(url);
    }

    public Image getImage(Long imageId) {
        return imageReader.getImage(imageId);
    }
}
