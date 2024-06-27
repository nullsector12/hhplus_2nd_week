package com.hhplus.assignment.lecture.domain.lecture.service;

import com.hhplus.assignment.lecture.domain.lecture.model.dto.LectureDto;
import com.hhplus.assignment.lecture.domain.lecture.model.dto.LectureEnrollHistoryDto;
import com.hhplus.assignment.lecture.domain.lecture.model.dto.LectureResponse;
import com.hhplus.assignment.lecture.domain.lecture.model.dto.OpenCourseDto;
import com.hhplus.assignment.lecture.domain.lecture.model.entity.Lecture;
import com.hhplus.assignment.lecture.domain.lecture.model.entity.LectureEnrollmentId;
import com.hhplus.assignment.lecture.domain.lecture.model.entity.OpenCourse;
import com.hhplus.assignment.lecture.domain.lecture.model.param.CheckEnrollCourseRequestParam;
import com.hhplus.assignment.lecture.domain.lecture.model.param.LectureEnrollRequestParam;
import com.hhplus.assignment.lecture.domain.lecture.model.param.LectureOpenCourseSearchRequestParam;
import com.hhplus.assignment.lecture.domain.lecture.repository.*;
import com.hhplus.assignment.lecture.exception.LectureException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(value = MockitoExtension.class)
public class LectureServiceTest {

    @Mock
    private LectureEnrollHistoryRepository lectureEnrollHistoryRepository;

    @Mock
    private OpenCourseRepository openCourseRepository;

    @InjectMocks
    private LectureService lectureService;

    private LectureDto lecture;

    @BeforeEach
    void setUp() {
        lecture = new LectureDto(1L, "강의 제목");
    }

    @Test
    @DisplayName("강의 등록 테스트 - 특정 유저의 강의 등록 내역을 조회하고 이전 강의 등록 내역이 없을 경우 강의를 등록한다.")
    void enrollLecture() {
        // given
        long userId = 1L;
        long openCourseId = 1L;
        LectureEnrollRequestParam request = new LectureEnrollRequestParam(openCourseId);

        OpenCourseDto openCourse = new OpenCourseDto(1L, lecture.getLectureId(), 30, 0, LocalDateTime.now());
        when(openCourseRepository.findById(openCourseId)).thenReturn(openCourse);
        when(openCourseRepository.isFull(openCourse.getMaxStudentCount(), openCourse.getCapacity())).thenReturn(false);
        when(lectureEnrollHistoryRepository.isAlreadyEnrolled(userId, openCourse.getCourseId())).thenReturn(false);
        OpenCourseDto increaseCourse = new OpenCourseDto(1L, lecture.getLectureId(), 30, 1, LocalDateTime.now());
        when(openCourseRepository.increaseEnrollStudentCount(openCourse.getCourseId())).thenReturn(increaseCourse);
        LectureEnrollHistoryDto enrollHistory = new LectureEnrollHistoryDto(userId, openCourseId, LocalDateTime.now());
        when(lectureEnrollHistoryRepository.enrollCourse(userId, openCourse.getCourseId())).thenReturn(enrollHistory);

        // when
        LectureResponse<LectureEnrollHistoryDto> result = lectureService.enrollLecture(userId, request);

        // then
        assertThat(result.getData().getUserId()).isEqualTo(userId);
        assertThat(result.getData().getCourseId()).isEqualTo(openCourseId);
    }

    @Test
    @DisplayName("강의 등록 테스트 - 특정 유저의 강의 등록 내역을 조회하고 정원을 초과한 경우 예외를 발생시킨다.")
    void enrollLectureOverMaxEnrollStudent() {
        // given
        long userId = 1L;
        long openCourseId = 1L;
        LectureEnrollRequestParam request = new LectureEnrollRequestParam(openCourseId);

        OpenCourseDto openCourse = new OpenCourseDto(1L, lecture.getLectureId(), 30, 30, LocalDateTime.now());
        when(openCourseRepository.findById(openCourseId)).thenReturn(openCourse);

        // when
        when(openCourseRepository.isFull(openCourse.getMaxStudentCount(), openCourse.getCapacity()))
                .thenThrow(new LectureException(HttpStatus.BAD_REQUEST, "FULL_CAPACITY", "수강 정원이 가득찼습니다."));

        // then
        Assertions.assertThrows(LectureException.class, () -> lectureService.enrollLecture(userId, request));
    }

    @Test
    @DisplayName("강의 등록 테스트 - 특정 유저의 강의 등록 내역을 조회하고 이미 해당 강의에 등록된 경우 예외를 발생시킨다.")
    void enrollLectureAlready() {
        // given
        long userId = 1L;
        long openCourseId = 1L;
        LectureEnrollRequestParam request = new LectureEnrollRequestParam(openCourseId);

        OpenCourseDto openCourse = new OpenCourseDto(1L, lecture.getLectureId(), 30, 29, LocalDateTime.now());
        when(openCourseRepository.findById(openCourseId)).thenReturn(openCourse);

        // when
        when(lectureEnrollHistoryRepository.isAlreadyEnrolled(userId, openCourse.getCourseId())).thenReturn(true)
                .thenThrow(new LectureException(HttpStatus.BAD_REQUEST, "ALREADY_ENROLLED", "이미 수강신청한 특강입니다."));

        // then
        Assertions.assertThrows(LectureException.class, () -> lectureService.enrollLecture(userId, request));
    }

    @Test
    @DisplayName("개설 강의 조회 테스트 - 특정 강의에 개설된 특강목록을 조회한다.")
    void openCourseList() {

        // given
        long lectureId = 1L;
        LectureOpenCourseSearchRequestParam param = new LectureOpenCourseSearchRequestParam(lectureId);
        List<OpenCourseDto> openCourseList = List.of(
                new OpenCourseDto(1L, lectureId, 30, 0, LocalDateTime.now()),
                new OpenCourseDto(2L, lectureId, 30, 0,
                        LocalDateTime.of(2024, 6, 28, 10, 0, 0))
        );
        when(openCourseRepository.findAllById(param.getLectureId())).thenReturn(openCourseList);

        // when
        LectureResponse<List<OpenCourseDto>> result = lectureService.openCourseList(param);

        // then
        assertThat(result.getData()).hasSize(2);
        assertThat(result.getData().get(0).getCourseId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("수강신청 확인 테스트 - 특정 유저가 수강신청한 강의가 존재하지 않아 예외를 발생시킨다.")
    void checkEnrolledCourseIsNotExist() {
        // given
        long userId = 1L;
        long openCourseId = 1L;
        LectureEnrollmentId lectureEnrollmentId = new LectureEnrollmentId(userId, openCourseId);
        CheckEnrollCourseRequestParam param = new CheckEnrollCourseRequestParam(openCourseId);

        // when
        when(lectureEnrollHistoryRepository.findByEnrollmentId(lectureEnrollmentId))
                .thenThrow(new LectureException(HttpStatus.NOT_FOUND, "HISTORY_NOT_FOUND", "해당 강의 수강 내역이 존재하지 않습니다."));

        // then
        Assertions.assertThrows(LectureException.class, () -> lectureService.checkEnrolledCourse(userId, param));
    }

    @Test
    @DisplayName("수강신청 확인 테스트 - 특정 유저가 수강신청한 강의를 확인한다.")
    void checkEnrolledCourse() {
        // given
        long userId = 1L;
        long openCourseId = 1L;
        CheckEnrollCourseRequestParam param = new CheckEnrollCourseRequestParam(openCourseId);
        LectureEnrollmentId lectureEnrollmentId = new LectureEnrollmentId(userId, openCourseId);
        LectureEnrollHistoryDto lectureEnrollHistory = new LectureEnrollHistoryDto(userId, openCourseId, LocalDateTime.now());
        when(lectureEnrollHistoryRepository.findByEnrollmentId(lectureEnrollmentId)).thenReturn(lectureEnrollHistory);

        // when
        LectureResponse<LectureEnrollHistoryDto> result = lectureService.checkEnrolledCourse(userId, param);

        // then
        assertThat(result.getData().getUserId()).isEqualTo(userId);
        assertThat(result.getData().getCourseId()).isEqualTo(openCourseId);
    }
}
