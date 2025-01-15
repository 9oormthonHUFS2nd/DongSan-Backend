package com.dongsan.domains.image.usecase;

import com.dongsan.common.annotation.UseCase;
import com.dongsan.domains.image.entity.Image;
import com.dongsan.domains.image.service.ImageCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@RequiredArgsConstructor
public class ImageUseCase {
    private final ImageCommandService imageCommandService;

    @Transactional
    public Image createImage(String url) {
        return imageCommandService.createImage(url);
    }
}
