package com.hhplus.assignment.lecture.domain.lecture.model.dto;

import com.hhplus.assignment.lecture.domain.lecture.model.entity.Lecture;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class LectureDto {

    private Long lectureId;
    private String lectureTitle;

    public LectureDto (Lecture lecture) {
        this.lectureId = lecture.getId();
        this.lectureTitle = lecture.getTitle();
    }

}
