package com.hhplus.assignment.lecture.domain.lecture.model.dto;

import com.hhplus.assignment.lecture.domain.lecture.model.entity.OpenCourse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class OpenCourseDto {

    private Long courseId;
    private Long lectureId;
    private Integer maxStudentCount;
    private Integer capacity;
    private LocalDateTime openAt;

    public OpenCourseDto(OpenCourse openCourse) {
        this.courseId = openCourse.getId();
        this.lectureId = openCourse.getLectureId();
        this.maxStudentCount = openCourse.getMaxStudentCount();
        this.capacity = openCourse.getCapacity();
        this.openAt = openCourse.getOpenAt();
    }
}
