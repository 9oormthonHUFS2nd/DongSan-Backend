package com.dongsan.common.validation.validator;

import com.dongsan.common.validation.annotation.NotEmptyWalkwayCourse;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class NotEmptyCourseValidator implements ConstraintValidator<NotEmptyWalkwayCourse, List<List<Double>>> {
    @Override
    public boolean isValid(List<List<Double>> course, ConstraintValidatorContext context) {
        // 코스가 null이면 유효하지 않음
        if (course == null || course.isEmpty()) {
            return false;
        }

        // 각 내부 리스트가 null이 아니고, 비어있지 않으며, 좌표가 null이 아닌지 확인
        for (List<Double> coordinates : course) {
            if (coordinates == null || coordinates.size() != 2 || coordinates.contains(null)) {
                return false;
            }
        }

        return true;
    }
}
