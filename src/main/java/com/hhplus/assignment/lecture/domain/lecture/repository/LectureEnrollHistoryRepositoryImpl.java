package com.hhplus.assignment.lecture.domain.lecture.repository;

import com.hhplus.assignment.lecture.domain.lecture.model.dto.LectureEnrollHistoryDto;
import com.hhplus.assignment.lecture.domain.lecture.model.entity.LectureEnrollmentHistory;
import com.hhplus.assignment.lecture.domain.lecture.model.entity.LectureEnrollmentId;
import com.hhplus.assignment.lecture.exception.LectureException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class LectureEnrollHistoryRepositoryImpl implements LectureEnrollHistoryRepository{

    private final LectureEnrollHistoryJpaRepository lectureEnrollHistoryJpaRepository;

    @Override
    public List<LectureEnrollHistoryDto> findByUserId(Long userId) {
        return lectureEnrollHistoryJpaRepository.findAllById_userId(userId)
                .stream().map(LectureEnrollHistoryDto::new).toList();
    }

    @Override
    public LectureEnrollHistoryDto findByEnrollmentId(LectureEnrollmentId enrollmentId) {
        return new LectureEnrollHistoryDto(lectureEnrollHistoryJpaRepository.findById(enrollmentId)
                .orElseThrow(() -> new LectureException(HttpStatus.NOT_FOUND, "HISTORY_NOT_FOUND", "해당 강의 수강 내역이 존재하지 않습니다."))
        );
    }

    @Override
    public LectureEnrollHistoryDto enrollCourse(Long userId, Long courseId) {

        return new LectureEnrollHistoryDto(
                lectureEnrollHistoryJpaRepository.save(
                        LectureEnrollmentHistory.builder()
                                .id(LectureEnrollmentId.builder()
                                        .userId(userId)
                                        .courseId(courseId)
                                        .build())
                                .build()
                ));
    }

    @Override
    public boolean isAlreadyEnrolled(Long userId, Long courseId) {
        return lectureEnrollHistoryJpaRepository.existsById(LectureEnrollmentId.builder()
                .userId(userId)
                .courseId(courseId)
                .build());
    }
}
