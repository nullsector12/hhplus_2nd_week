package com.hhplus.assignment.lecture.domain.lecture.repository;

import com.hhplus.assignment.lecture.domain.lecture.model.entity.LectureEnrollmentId;
import com.hhplus.assignment.lecture.domain.lecture.model.entity.LectureEnrollmentHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LectureEnrollHistoryJpaRepository extends JpaRepository<LectureEnrollmentHistory, LectureEnrollmentId> {
    List<LectureEnrollmentHistory> findAllById_userId(Long userId);
}
