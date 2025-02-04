package com.dongsan.domains.image.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.dongsan.core.domains.image.ImageUseCase;
import com.dongsan.domains.image.entity.Image;
import com.dongsan.core.domains.image.ImageCommandService;
import fixture.ImageFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("ImageUseCase Unit Test")
public class ImageUseCaseTest {
    @Mock
    ImageCommandService imageCommandService;
    @InjectMocks
    ImageUseCase imageUseCase;

    @Nested
    @DisplayName("createImage 메서드는")
    class Describe_createImage {
        @Test
        @DisplayName("이미지 url을 입력 받으면 Image를 생성한다.")
        void it_returns_entity() {
            // Given
            Long id = 1L;
            String url = "test.com";
            Image image = ImageFixture.createImageWithId(id, url);

            when(imageCommandService.createImage(url)).thenReturn(image);

            // When
            Image result = imageUseCase.createImage(url);

            // Then
            assertThat(result.getId()).isEqualTo(id);
            assertThat(result.getUrl()).isEqualTo(url);
        }
    }
}
