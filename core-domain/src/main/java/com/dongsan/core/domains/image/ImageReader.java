package com.dongsan.core.domains.image;

import com.dongsan.common.error.code.ImageErrorCode;
import com.dongsan.common.error.exception.CustomException;
import com.dongsan.domains.image.entity.Image;
import com.dongsan.domains.image.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ImageReader {
    private final ImageRepository imageRepository;

    public Image getImage(Long id) {
        return imageRepository.findById(id)
                .orElseThrow(() -> new CustomException(ImageErrorCode.IMAGE_NOT_EXISTS));
    }
}
