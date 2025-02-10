package com.dongsan.rdb.domains.image;

import com.dongsan.core.common.error.CoreErrorCode;
import com.dongsan.core.common.error.CoreException;
import com.dongsan.core.domains.image.Image;
import com.dongsan.core.domains.image.ImageRepository;
import com.dongsan.rdb.domains.image.entity.ImageEntity;
import com.dongsan.rdb.domains.image.mapper.ImageMapper;
import com.dongsan.rdb.domains.image.repository.ImageJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ImageCoreRepository implements ImageRepository {
    private final ImageJpaRepository imageJpaRepository;

    public Image findById(Long imageId) {
        ImageEntity imageEntity = imageJpaRepository.findById(imageId)
                .orElseThrow(() -> new CoreException(CoreErrorCode.IMAGE_NOT_EXISTS));

        return ImageMapper.toImage(imageEntity);
    }

}
