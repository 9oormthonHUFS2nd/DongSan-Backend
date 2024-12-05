package com.dongsan.common.validation.validator;

import com.dongsan.common.validation.annotation.ExistBookmark;
import com.dongsan.domains.bookmark.service.BookmarkQueryService;
import com.dongsan.error.code.BookmarkErrorCode;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExistBookmarkValidator implements ConstraintValidator<ExistBookmark, Long> {
    private final BookmarkQueryService bookmarkQueryService;
    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        boolean isValid = false;
        if(value != null){
            isValid = bookmarkQueryService.existsById(value);
        }

        if(!isValid){
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                            BookmarkErrorCode.BOOKMARK_NOT_EXIST.toString())
                    .addConstraintViolation();
        }

        return isValid;
    }
}
