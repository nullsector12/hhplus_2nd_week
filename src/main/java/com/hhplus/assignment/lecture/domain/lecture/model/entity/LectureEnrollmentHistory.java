package com.hhplus.assignment.lecture.domain.lecture.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Entity
@Embeddable
@EqualsAndHashCode(of={"id"})
public class LectureEnrollmentHistory {

    @EmbeddedId
    private LectureEnrollmentId id;

    @Column(name = "enrolled_at", columnDefinition = "DATETIME(3) DEFAULT CURRENT_TIMESTAMP(3)")
    private LocalDateTime enrolledAt;
}
