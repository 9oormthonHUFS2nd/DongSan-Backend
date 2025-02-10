//package com.dongsan.domains.image.service;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.assertThatThrownBy;
//import static org.mockito.Mockito.when;
//
//import com.dongsan.common.error.exception.CustomException;
//import com.dongsan.core.domains.image.ImageReader;
//import com.dongsan.domains.image.entity.Image;
//import com.dongsan.domains.image.repository.ImageRepository;
//import fixture.ImageFixture;
//import java.util.Optional;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//@ExtendWith(MockitoExtension.class)
//@DisplayName("ImageCommandService Unit Test")
//public class ImageReaderTest {
//    @Mock
//    private ImageRepository imageRepository;
//    @InjectMocks
//    private ImageReader imageReader;
//
//    @Nested
//    @DisplayName("getImage 메서드는")
//    class Describe_getImage {
//        @Test
//        @DisplayName("아이디를 입력하면 해당 엔티티를 반환한다.")
//        void it_returns_entity() {
//            // Given
//            Long id = 1L;
//            Image image = ImageFixture.createImageWithId(id);
//
//            when(imageRepository.findById(id)).thenReturn(Optional.of(image));
//
//            // When
//            Image result = imageReader.getImage(id);
//
//            // Then
//            assertThat(result.getId()).isEqualTo(id);
//        }
//
//        @Test
//        @DisplayName("아이디에 해당하는 엔티티를 반환한다.")
//        void it_returns_exception() {
//            // Given
//            Long id = 1L;
//
//            when(imageRepository.findById(id)).thenReturn(Optional.empty());
//
//            // When && Then
//            assertThatThrownBy(() -> imageReader.getImage(id))
//                    .isInstanceOf(CustomException.class);
//        }
//    }
//}
