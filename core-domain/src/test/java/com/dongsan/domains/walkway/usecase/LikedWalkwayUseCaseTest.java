//package com.dongsan.domains.walkway.usecase;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//import com.dongsan.core.domains.walkway.usecase.LikedWalkwayService;
//import com.dongsan.domains.member.entity.Member;
//import com.dongsan.core.domains.member.MemberReader;
//import com.dongsan.domains.walkway.entity.Walkway;
//import com.dongsan.core.domains.walkway.service.LikedWalkwayWriter;
//import com.dongsan.core.domains.walkway.service.LikedWalkwayReader;
//import com.dongsan.core.domains.walkway.service.WalkwayReader;
//import fixture.MemberFixture;
//import fixture.WalkwayFixture;
//import java.util.ArrayList;
//import java.util.List;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//@ExtendWith(MockitoExtension.class)
//@DisplayName("LikedWalkwayUseCase Unit Test")
//class LikedWalkwayUseCaseTest {
//    @Mock
//    LikedWalkwayWriter likedWalkwayWriter;
//    @Mock
//    MemberReader memberReader;
//    @Mock
//    WalkwayReader walkwayQueryService;
//    @Mock
//    LikedWalkwayReader likedWalkwayQueryService;
//    @InjectMocks
//    LikedWalkwayService likedWalkwayUseCase;
//
//    @Nested
//    @DisplayName("createLikedWalkway 메서드는")
//    class Describe_createLikedWalkway {
//        @Test
//        @DisplayName("회원과 산책로가 존재하면 좋아요를 생성한다.")
//        void it_create_liked() {
//            // Given
//            Member member = MemberFixture.createMember();
//            Walkway walkway = WalkwayFixture.createWalkway(member);
//
//            when(memberReader.getMember(member.getId())).thenReturn(member);
//            when(walkwayQueryService.getWalkway(walkway.getId())).thenReturn(walkway);
//            when(likedWalkwayWriter.existsLikedWalkwayByMemberAndWalkway(member, walkway)).thenReturn(false);
//
//            // When
//            likedWalkwayUseCase.createLikedWalkway(member.getId(), walkway.getId());
//
//            // Then
//            verify(likedWalkwayWriter).createLikedWalkway(any());
//        }
//    }
//
//    @Nested
//    @DisplayName("deleteLikedWalkway 메서드는")
//    class Describe_deleteLikedWalkway {
//        @Test
//        @DisplayName("회원과 산책로가 존재하면 좋아요를 삭제한다.")
//        void it_delete_liked() {
//            // Given
//            Member member = MemberFixture.createMember();
//            Walkway walkway = WalkwayFixture.createWalkway(member);
//
//            when(memberReader.getMember(member.getId())).thenReturn(member);
//            when(walkwayQueryService.getWalkway(walkway.getId())).thenReturn(walkway);
//            when(likedWalkwayWriter.existsLikedWalkwayByMemberAndWalkway(member, walkway)).thenReturn(true);
//
//            // When
//            likedWalkwayUseCase.deleteLikedWalkway(member.getId(), walkway.getId());
//
//            // Then
//            verify(likedWalkwayWriter).deleteLikedWalkway(member, walkway);
//        }
//    }
//
//    @Nested
//    @DisplayName("existsLikedWalkways 메서드는")
//    class Describe_existsLikedWalkways {
//        @Test
//        @DisplayName("회원의 산책로 좋아요 여부 리스트를 반환한다..")
//        void it_returns_liked_exists_list() {
//            // Given
//            Member member = MemberFixture.createMemberWithId(1L);
//            List<Walkway> walkways = new ArrayList<>();
//            for (long id = 0; id < 10; id++) {
//                walkways.add(WalkwayFixture.createWalkwayWithId(id, member));
//            }
//
//            for (long id = 0; id < 10; id++) {
//                when(likedWalkwayQueryService.existsByMemberIdAndWalkwayId(member.getId(), id)).thenReturn(true);
//            }
//
//            // When
//            List<Boolean> result = likedWalkwayUseCase.existsLikedWalkways(member.getId(), walkways);
//
//            // Then
//            assertThat(result).hasSize(10);
//            for (int index = 0; index < result.size(); index++) {
//                assertThat(result.get(index)).isTrue();
//            }
//
//        }
//    }
//}