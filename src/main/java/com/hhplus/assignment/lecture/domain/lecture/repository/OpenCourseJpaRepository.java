package com.hhplus.assignment.lecture.domain.lecture.repository;

import com.hhplus.assignment.lecture.domain.lecture.model.entity.OpenCourse;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.Optional;

public interface OpenCourseJpaRepository extends JpaRepository<OpenCourse, Long> {

    Collection<OpenCourse> findAllByLectureId(Long lectureId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select oc from OpenCourse oc where oc.id = :courseId")
    Optional<OpenCourse> findByIdForUpdate(@Param("courseId") Long courseId);
}
