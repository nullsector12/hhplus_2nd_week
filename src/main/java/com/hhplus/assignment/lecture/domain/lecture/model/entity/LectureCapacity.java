package com.hhplus.assignment.lecture.domain.lecture.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Entity
public class LectureCapacity {

    @Id
    @Column(name = "lecture_id")
    private Long lectureId;

    private int capacity;

    public void increaseEnrolledStudentCount(int studentCount) {
        this.capacity = studentCount;
    }
}
