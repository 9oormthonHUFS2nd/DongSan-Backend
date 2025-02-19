package com.dongsan.api.domains.image;

import com.dongsan.file.service.S3FileService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageService {
    private final S3FileService s3FileService;

    public ImageService(S3FileService s3FileService) {
        this.s3FileService = s3FileService;
    }

    public String uploadCourseImage(MultipartFile image) {
        return s3FileService.saveFile(image);
    }
}
