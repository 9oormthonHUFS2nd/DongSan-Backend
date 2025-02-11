package com.dongsan.core.domains.image;

import com.dongsan.domains.image.entity.Image;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class ImageService {
    private final ImageWriter imageWriter;

    @Transactional
    public Image createImage(String url) {
        return imageWriter.createImage(url);
    }
}
