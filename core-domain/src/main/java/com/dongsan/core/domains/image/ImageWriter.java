package com.dongsan.core.domains.image;

import com.dongsan.domains.image.entity.Image;
import com.dongsan.domains.image.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ImageWriter {
    private final ImageRepository imageRepository;

    public Image createImage(String imageUrl) {
        return imageRepository.save(Image.builder()
                .url(imageUrl)
                .build()
        );
    }
}
