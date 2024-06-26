package com.hhplus.assignment.lecture.domain.lecture.repository;

import com.hhplus.assignment.lecture.domain.lecture.model.dto.LectureDto;
import com.hhplus.assignment.lecture.domain.lecture.model.entity.Lecture;
import com.hhplus.assignment.lecture.domain.lecture.model.param.LectureResisterRequestParam;
import com.hhplus.assignment.lecture.exception.LectureException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class LectureRepositoryImpl implements LectureRepository {

    private final LectureJpaRepository lectureJpaRepository;

    @Override
    public LectureDto findById(Long lectureId) {
        return new LectureDto(lectureJpaRepository.findById(lectureId)
                .orElseThrow(() -> new LectureException(HttpStatus.NOT_FOUND, "LECTURE_NOT_FOUND", "해당 강의가 존재하지 않습니다."))
        );
    }

    @Override
    public LectureDto registerLecture(LectureResisterRequestParam request) {
        return new LectureDto(lectureJpaRepository.save(Lecture.builder()
                .title(request.getTitle())
                .build()));
    }
}
