package com.dongsan.core.domains.bookmark;

import com.dongsan.core.domains.common.Author;

public record Bookmark(
        Long bookmarkId,
        String title,
        Author author
) {
}
