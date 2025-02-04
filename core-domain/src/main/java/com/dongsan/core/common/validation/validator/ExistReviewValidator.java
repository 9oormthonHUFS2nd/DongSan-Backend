package com.dongsan.core.common.validation.validator;

import com.dongsan.core.common.validation.annotation.ExistReview;
import com.dongsan.core.domains.review.ReviewReader;
import com.dongsan.common.error.code.ReviewErrorCode;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExistReviewValidator implements ConstraintValidator<ExistReview, Long> {
    private final ReviewReader reviewReader;

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        boolean isValid = false;
        if(value != null){
            isValid = reviewReader.existsByReviewId(value);
        }

        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                            ReviewErrorCode.REVIEW_NOT_FOUND.toString())
                    .addConstraintViolation();
        }

        return isValid;
    }
}
