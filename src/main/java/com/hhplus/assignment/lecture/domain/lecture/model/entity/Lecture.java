package com.hhplus.assignment.lecture.domain.lecture.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Entity
public class Lecture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(name = "max_student_count")
    private int maxStudentCount;

    @Column(name = "created_at", columnDefinition = "DATETIME(3) DEFAULT CURRENT_TIMESTAMP(3)")
    private LocalDateTime createdAt;

    @Builder
    public Lecture (String title, int maxStudentCount) {
        this.title = title;
        this.maxStudentCount = maxStudentCount;
    }

}
