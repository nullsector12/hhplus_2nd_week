package com.hhplus.assignment.lecture.domain.lecture.repository;

import com.hhplus.assignment.lecture.domain.lecture.model.entity.LectureEnrollmentId;
import com.hhplus.assignment.lecture.domain.lecture.model.entity.LectureEnrollmentHistory;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.List;

public interface LectureEnrollHistoryJpaRepository extends JpaRepository<LectureEnrollmentHistory, LectureEnrollmentId> {
    List<LectureEnrollmentHistory> findAllById_userId(Long userId);
}
