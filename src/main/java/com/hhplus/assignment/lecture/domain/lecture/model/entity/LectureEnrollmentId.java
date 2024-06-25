package com.hhplus.assignment.lecture.domain.lecture.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Embeddable
@Builder
public class LectureEnrollmentId implements Serializable {

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "lecture_id")
    private Long lectureId;
}
