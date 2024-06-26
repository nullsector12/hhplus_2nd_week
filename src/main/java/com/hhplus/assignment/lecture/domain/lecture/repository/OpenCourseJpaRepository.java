package com.hhplus.assignment.lecture.domain.lecture.repository;

import com.hhplus.assignment.lecture.domain.lecture.model.entity.OpenCourse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface OpenCourseJpaRepository extends JpaRepository<OpenCourse, Long> {

    Collection<OpenCourse> findAllByLectureId(Long lectureId);
}
