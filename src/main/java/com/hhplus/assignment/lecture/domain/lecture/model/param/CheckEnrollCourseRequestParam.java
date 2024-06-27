package com.hhplus.assignment.lecture.domain.lecture.model.param;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CheckEnrollCourseRequestParam {

    private Long courseId;

    @Builder
    public CheckEnrollCourseRequestParam(Long courseId) {
        this.courseId = courseId;
    }
}
