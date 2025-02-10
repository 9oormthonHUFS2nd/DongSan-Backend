package com.dongsan.rdb.domains.image.mapper;

import com.dongsan.core.domains.image.Image;
import com.dongsan.rdb.domains.image.entity.ImageEntity;

public class ImageMapper {
    private ImageMapper(){}
    public static Image toImage(ImageEntity imageEntity) {
        return Image.builder()
                .url(imageEntity.getUrl())
                .build();
    }
}
