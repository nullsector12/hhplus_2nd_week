package com.hhplus.assignment.lecture.domain.lecture.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
public class OpenCourse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "lecture_id")
    private Long lectureId;

    @Column(name = "max_student_count", updatable = false)
    private Integer maxStudentCount;

    private Integer capacity;

    @Column(name = "open_at")
    private LocalDateTime openAt;

    public void increaseEnrolledStudentCount(int studentCount) {
        this.capacity = studentCount;
    }

    @Builder
    public OpenCourse (Long lectureId, Integer maxStudentCount, LocalDateTime openAt) {
        this.lectureId = lectureId;
        this.maxStudentCount = maxStudentCount;
        this.capacity = 0;
        this.openAt = openAt;
    }
}
