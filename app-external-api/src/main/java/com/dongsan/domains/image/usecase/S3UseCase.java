package com.dongsan.domains.image.usecase;

import com.dongsan.common.annotation.UseCase;
import com.dongsan.service.S3FileService;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@UseCase
@RequiredArgsConstructor
public class S3UseCase {

    private final S3FileService s3FileService;

    @Transactional
    public String uploadCourseImage(MultipartFile image) throws IOException {
        return s3FileService.saveFile(image);
    }
}
