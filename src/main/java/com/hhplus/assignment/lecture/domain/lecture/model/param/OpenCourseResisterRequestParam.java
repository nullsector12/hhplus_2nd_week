package com.hhplus.assignment.lecture.domain.lecture.model.param;

import lombok.Data;

@Data
public class OpenCourseResisterRequestParam {

    private Long lectureId;
    private Integer maxStudentCount;
    private Integer capacity;
    private String openAt;
}
