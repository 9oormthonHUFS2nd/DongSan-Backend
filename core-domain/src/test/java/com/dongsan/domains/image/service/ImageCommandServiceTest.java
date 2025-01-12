package com.dongsan.domains.image.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.dongsan.domains.image.entity.Image;
import com.dongsan.domains.image.repository.ImageRepository;
import fixture.ImageFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("ImageCommandService Unit Test")
public class ImageCommandServiceTest {
    @Mock
    private ImageRepository imageRepository;
    @InjectMocks
    private ImageCommandService imageCommandService;

    @Nested
    @DisplayName("createImage 메서드는")
    class Describe_createImage {
        @Test
        @DisplayName("이미지 Url을 입력 받으면 엔티티를 생성한다.")
        void it_returns_Entity() {
            // Given
            Long id = 1L;
            String url = "test.com";
            Image image = ImageFixture.createImageWithId(id, url);

            when(imageRepository.save(any())).thenReturn(image);

            // When
            Image result = imageCommandService.createImage(url);

            // Then
            assertThat(result.getId()).isEqualTo(id);
            assertThat(result.getUrl()).isEqualTo(url);
        }
    }
}
