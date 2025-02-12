package com.dongsan.api.domains.image;

import com.dongsan.file.service.S3FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
public class S3UseCase {

    private final S3FileService s3FileService;

    public String uploadCourseImage(MultipartFile image) {
        return s3FileService.saveFile(image);
    }
}
