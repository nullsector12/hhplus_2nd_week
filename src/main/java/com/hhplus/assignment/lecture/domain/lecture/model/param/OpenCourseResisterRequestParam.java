package com.hhplus.assignment.lecture.domain.lecture.model.param;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OpenCourseResisterRequestParam {

    private Long lectureId;
    private Integer maxStudentCount;
    private Integer capacity;
    private String openAt;

    @Builder
    public OpenCourseResisterRequestParam(Long lectureId, Integer maxStudentCount, Integer capacity, String openAt) {
        this.lectureId = lectureId;
        this.maxStudentCount = maxStudentCount;
        this.capacity = capacity;
        this.openAt = openAt;
    }
}
