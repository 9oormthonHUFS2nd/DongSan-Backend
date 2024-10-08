package com.dongsan.domains.user.dto;

import com.dongsan.domains.bookmark.entity.Bookmark;

import java.util.List;

public class UserBookmarkDto {

    public record UserBookmarkRes(Long bookmarkId, String title) {
        public static UserBookmarkRes of(Bookmark bookmark) {
            return new UserBookmarkRes(bookmark.getId(), bookmark.getName());
        }
    }

    public record UserBookmarksRes(List<UserBookmarkRes> bookmarks) {
    }
}
