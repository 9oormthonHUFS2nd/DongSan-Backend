package com.dongsan.core.common.validation.validator;

import com.dongsan.core.common.validation.annotation.ExistWalkway;
import com.dongsan.core.domains.walkway.service.WalkwayReader;
import com.dongsan.common.error.code.WalkwayErrorCode;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExistWalkwayValidator implements ConstraintValidator<ExistWalkway, Long> {
    private final WalkwayReader walkwayQueryService;
    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        boolean isValid = false;
        if(value != null){
            isValid = walkwayQueryService.existsWalkway(value);
        }
        if(!isValid){
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    WalkwayErrorCode.WALKWAY_NOT_FOUND.toString()
            ).addConstraintViolation();
        }

        return isValid;
    }
}
