package com.hhplus.assignment.lecture.domain.lecture.repository;

import com.hhplus.assignment.lecture.domain.lecture.model.entity.LectureEnrollmentId;
import com.hhplus.assignment.lecture.domain.lecture.model.entity.LectureEnrollmentList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LectureEnrollRepository extends JpaRepository<LectureEnrollmentList, LectureEnrollmentId> {
}
