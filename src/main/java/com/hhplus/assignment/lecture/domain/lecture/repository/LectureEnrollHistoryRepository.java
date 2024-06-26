package com.hhplus.assignment.lecture.domain.lecture.repository;

import com.hhplus.assignment.lecture.domain.lecture.model.dto.LectureEnrollHistoryDto;
import com.hhplus.assignment.lecture.domain.lecture.model.entity.LectureEnrollmentId;

import java.util.List;

public interface LectureEnrollHistoryRepository {

    List<LectureEnrollHistoryDto> findByUserId(Long userId);

    LectureEnrollHistoryDto findByEnrollmentId(LectureEnrollmentId enrollmentId);

    LectureEnrollHistoryDto enrollCourse(Long userId, Long courseId);

    boolean isAlreadyEnrolled(Long userId, Long courseId);

}
