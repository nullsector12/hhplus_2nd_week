package com.hhplus.assignment.lecture.domain.lecture.model.dto;

import com.hhplus.assignment.lecture.domain.lecture.model.entity.LectureEnrollmentHistory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LectureEnrollHistoryDto {

    private Long userId;
    private Long courseId;
    private LocalDateTime enrolledAt;

    public LectureEnrollHistoryDto(LectureEnrollmentHistory history) {
        this.userId = history.getId().getUserId();
        this.courseId = history.getId().getCourseId();
        this.enrolledAt = history.getEnrolledAt();
    }
}
