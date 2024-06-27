package com.hhplus.assignment.lecture.domain.lecture.repository;

import com.hhplus.assignment.lecture.common.dateTimeUtils.DateTimeUtils;
import com.hhplus.assignment.lecture.domain.lecture.model.dto.OpenCourseDto;
import com.hhplus.assignment.lecture.domain.lecture.model.entity.OpenCourse;
import com.hhplus.assignment.lecture.domain.lecture.model.param.OpenCourseResisterRequestParam;
import com.hhplus.assignment.lecture.exception.LectureException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OpenCourseRepositoryImpl implements OpenCourseRepository{

    private final OpenCourseJpaRepository openCourseJpaRepository;

    @Override
    public OpenCourseDto findById(Long courseId) {
        return new OpenCourseDto(openCourseJpaRepository.findById(courseId)
                .orElseThrow(() -> new LectureException(HttpStatus.NOT_FOUND, "COURSE_NOT_FOUND", "개설된 강의가 존재하지 않습니다."))
        );
    }

    @Override
    public OpenCourseDto findByIdForUpdate(Long courseId) {
        return new OpenCourseDto(openCourseJpaRepository.findByIdForUpdate(courseId)
                .orElseThrow(() -> new LectureException(HttpStatus.NOT_FOUND, "COURSE_NOT_FOUND", "개설된 강의가 존재하지 않습니다."))
        );
    }

    @Override
    public List<OpenCourseDto> findAllById(Long lectureId) {
        return openCourseJpaRepository.findAllByLectureId(lectureId)
                .stream().map(OpenCourseDto::new).toList();
    }

    @Override
    public OpenCourseDto registerCourse(OpenCourseResisterRequestParam courseId) {
        OpenCourse openCourse = openCourseJpaRepository.save(
                OpenCourse.builder()
                        .lectureId(courseId.getLectureId())
                        .maxStudentCount(courseId.getMaxStudentCount())
                        .openAt(DateTimeUtils.stringToLocalDateTime(courseId.getOpenAt(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                        .build()
        );

        return new OpenCourseDto(openCourse);
    }

    @Override
    public boolean isFull(Integer maxStudentCount, Integer currentStudentCount) {
        return maxStudentCount.equals(currentStudentCount);
    }

    @Override
    public OpenCourseDto increaseEnrollStudentCount(Long courseId) {
        OpenCourse openCourse = openCourseJpaRepository.findById(courseId)
                .orElseThrow(() -> new LectureException(HttpStatus.NOT_FOUND, "COURSE_NOT_FOUND", "개설된 강의가 존재하지 않습니다."));

        openCourse.increaseEnrolledStudentCount(openCourse.getCapacity() + 1);
        return new OpenCourseDto(openCourse);
    }

    @Override
    public void deleteAllCourses() {
        openCourseJpaRepository.deleteAll();
    }
}
