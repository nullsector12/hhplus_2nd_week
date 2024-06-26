package com.hhplus.assignment.lecture.domain.lecture.repository;

import com.hhplus.assignment.lecture.domain.lecture.model.dto.LectureDto;
import com.hhplus.assignment.lecture.domain.lecture.model.param.LectureResisterRequestParam;

public interface LectureRepository  {

    LectureDto findById(Long lectureId);

    LectureDto registerLecture(LectureResisterRequestParam request);

}
