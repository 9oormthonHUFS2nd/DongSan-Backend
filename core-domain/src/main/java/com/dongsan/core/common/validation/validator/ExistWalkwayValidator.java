package com.dongsan.core.common.validation.validator;

import com.dongsan.core.common.validation.annotation.ExistWalkway;
import com.dongsan.core.domains.walkway.service.WalkwayQueryService;
import com.dongsan.common.error.code.WalkwayErrorCode;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExistWalkwayValidator implements ConstraintValidator<ExistWalkway, Long> {
    private final WalkwayQueryService walkwayQueryService;
    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        boolean isValid = false;
        if(value != null){
            isValid = walkwayQueryService.existsByWalkwayId(value);
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
