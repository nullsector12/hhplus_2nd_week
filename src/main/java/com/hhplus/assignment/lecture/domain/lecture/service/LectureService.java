package com.hhplus.assignment.lecture.domain.lecture.service;

import com.hhplus.assignment.lecture.domain.lecture.model.dto.LectureDto;
import com.hhplus.assignment.lecture.domain.lecture.model.dto.LectureEnrollHistoryDto;
import com.hhplus.assignment.lecture.domain.lecture.model.dto.LectureResponse;
import com.hhplus.assignment.lecture.domain.lecture.model.dto.OpenCourseDto;
import com.hhplus.assignment.lecture.domain.lecture.model.entity.Lecture;
import com.hhplus.assignment.lecture.domain.lecture.model.entity.OpenCourse;
import com.hhplus.assignment.lecture.domain.lecture.model.entity.LectureEnrollmentId;
import com.hhplus.assignment.lecture.domain.lecture.model.entity.LectureEnrollmentHistory;
import com.hhplus.assignment.lecture.domain.lecture.model.param.CheckEnrollCourseRequestParam;
import com.hhplus.assignment.lecture.domain.lecture.model.param.LectureEnrollRequestParam;
import com.hhplus.assignment.lecture.domain.lecture.model.param.LectureOpenCourseSearchRequestParam;
import com.hhplus.assignment.lecture.domain.lecture.repository.*;
import com.hhplus.assignment.lecture.exception.LectureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class LectureService {

    private final LectureEnrollHistoryRepository lectureEnrollRepository;
    private final OpenCourseRepository openCourseRepository;

    // 특강 신청
    @Transactional
    public LectureResponse<LectureEnrollHistoryDto> enrollLecture(Long userId, LectureEnrollRequestParam request) {

        // lectureId 에 해당하는 특강 정보를 가져온다.
        // case 1. 특강이 존재하지 않을 경우
        // 해당 lectureId 에 해당하는 특강 정보가 존재하지 않을 경우, 특강이 존재하지 않는다는 에러 메시지를 반환한다.
//        LectureDto lecture = lectureRepositoryImpl.findById(request.getLectureId());

        // openCourseId 에 해당하는 개설강의를 가져온다.
        // openCourseId 에 해당하는 특강의 수강신청 인원을 가져온다.
        OpenCourseDto course = openCourseRepository.findById(request.getCourseId());
        // case 2. 수강신청 인원이 30명 이상인 경우
        // 해당 lectureId에 해당하는 특강의 수강신청 인원이 30명 이상일 경우, 수강신청 인원이 초과되었다는 에러 메시지를 반환한다.
        if (openCourseRepository.isFull(course.getMaxStudentCount(), course.getCapacity())) {
            log.error("수강 정원이 가득찼습니다.");
            throw new LectureException(HttpStatus.BAD_REQUEST, "FULL_CAPACITY", "수강 정원이 가득찼습니다.");
        }

        // userId와 lectureId를 이용하여 특강 신청정보를 가져온다.
        // case 3. 이미 신청한 경우
        // 해당 userId와 lectureId에 해당하는 특강 신청 정보가 이미 존재할 경우, 이미 신청한 특강이라는 에러 메시지를 반환한다.
        if (lectureEnrollRepository.isAlreadyEnrolled(userId, course.getCourseId())) {
            log.error("이미 수강신청한 특강입니다.");
            throw new LectureException(HttpStatus.BAD_REQUEST, "ALREADY_ENROLLED", "이미 수강신청한 강의입니다.");
        }

        // 수강신청 인원을 1명 증가시킨다.
        openCourseRepository.increaseEnrollStudentCount(course.getCourseId());

        // 특강 신청정보를 저장한다.
        LectureEnrollHistoryDto result = lectureEnrollRepository.enrollCourse(userId, course.getCourseId());

        return new LectureResponse<>(result, "특강 신청이 완료되었습니다.");
    }

    public LectureResponse<List<OpenCourseDto>> openCourseList(LectureOpenCourseSearchRequestParam request) {
        if(request.getLectureId() == null) {
            throw new LectureException(HttpStatus.BAD_REQUEST, "INVALID_PARAMETER", "강의 ID가 존재하지 않습니다.");
        }
        return new LectureResponse<>(openCourseRepository.findAllById(request.getLectureId()), "특강 목록 조회가 완료되었습니다.");
    }

    public LectureResponse<LectureEnrollHistoryDto> checkEnrolledCourse(long userId, CheckEnrollCourseRequestParam param) {
        if(param.getCourseId() == null) {
            throw new LectureException(HttpStatus.BAD_REQUEST, "INVALID_PARAMETER", "개설강의 ID가 입력되지 않았습니다.");
        }

        return new LectureResponse<>(lectureEnrollRepository.findByEnrollmentId(LectureEnrollmentId.builder()
                .userId(userId)
                .courseId(param.getCourseId())
                .build()), "특강 신청 내역이 존재합니다.");
    }
}
