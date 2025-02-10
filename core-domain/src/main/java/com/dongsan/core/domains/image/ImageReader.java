package com.dongsan.core.domains.image;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ImageReader {
    private final ImageRepository imageRepository;

    public Image getImage(Long id) {
        return imageRepository.findById(id);
    }
}
