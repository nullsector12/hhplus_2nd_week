package com.hhplus.assignment.lecture.domain.lecture.model.param;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class LectureEnrollRequestParam {

    private Long courseId;

    @Builder
    public LectureEnrollRequestParam(Long courseId) {
        this.courseId = courseId;
    }

}
