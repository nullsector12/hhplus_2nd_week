package com.hhplus.assignment.lecture.domain.lecture.repository;

import com.hhplus.assignment.lecture.domain.lecture.model.dto.OpenCourseDto;
import com.hhplus.assignment.lecture.domain.lecture.model.param.LectureOpenCourseSearchRequestParam;
import com.hhplus.assignment.lecture.domain.lecture.model.param.OpenCourseResisterRequestParam;

import java.util.List;

public interface OpenCourseRepository {

    // 개설 강의 찾기
    OpenCourseDto findById(Long courseId);

    // 개설 강의 전체 찾기
    List<OpenCourseDto> findAllById(Long lectureId);

    // 개설 강의 등록
    OpenCourseDto registerCourse(OpenCourseResisterRequestParam request);

    // 개설 강의의 정원이 가득 찾는지 확인하기
    boolean isFull(Integer maxStudentCount, Integer currentStudentCount);

    OpenCourseDto increaseEnrollStudentCount(Long courseId);


}
