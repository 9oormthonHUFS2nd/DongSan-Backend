package com.dongsan.domains.user.dto;

import java.util.List;

public class UserBookmarkDto {



    public record UserBookmarksRes(List<UserBookmarkRes> bookmarks) {
        public record UserBookmarkRes(Long bookmarkId, String title) {
            public static UserBookmarkRes of(com.dongsan.domains.bookmark.entity.Bookmark bookmark) {
                return new UserBookmarkRes(bookmark.getId(), bookmark.getName());
            }
        }
    }
}
