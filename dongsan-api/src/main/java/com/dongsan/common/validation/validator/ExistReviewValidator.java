package com.dongsan.common.validation.validator;

import com.dongsan.common.validation.annotation.ExistReview;
import com.dongsan.domains.review.service.ReviewQueryService;
import com.dongsan.error.code.ReviewErrorCode;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExistReviewValidator implements ConstraintValidator<ExistReview, Long> {
    private final ReviewQueryService reviewQueryService;

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        boolean isValid = reviewQueryService.existsByReviewId(value);

        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                            ReviewErrorCode.REVIEW_NOT_FOUND.toString())
                    .addConstraintViolation();
        }

        return isValid;
    }
}
