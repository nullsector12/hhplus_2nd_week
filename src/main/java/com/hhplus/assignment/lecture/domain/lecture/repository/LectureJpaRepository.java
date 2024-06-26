package com.hhplus.assignment.lecture.domain.lecture.repository;

import com.hhplus.assignment.lecture.domain.lecture.model.entity.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LectureJpaRepository extends JpaRepository<Lecture, Long> {

}
