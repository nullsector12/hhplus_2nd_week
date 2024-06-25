package com.hhplus.assignment.lecture.domain.lecture.service;

import com.hhplus.assignment.lecture.domain.lecture.model.entity.Lecture;
import com.hhplus.assignment.lecture.domain.lecture.model.entity.LectureCapacity;
import com.hhplus.assignment.lecture.domain.lecture.model.entity.LectureEnrollmentId;
import com.hhplus.assignment.lecture.domain.lecture.model.entity.LectureEnrollmentList;
import com.hhplus.assignment.lecture.domain.lecture.model.param.LectureEnrollRequestParam;
import com.hhplus.assignment.lecture.domain.lecture.repository.LectureCapacityRepository;
import com.hhplus.assignment.lecture.domain.lecture.repository.LectureEnrollRepository;
import com.hhplus.assignment.lecture.domain.lecture.repository.LectureRepository;
import com.hhplus.assignment.lecture.domain.user.model.dto.UserDto;
import com.hhplus.assignment.lecture.domain.user.model.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.beans.Transient;
import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LectureService {

    private final LectureRepository lectureRepository;
    private final LectureEnrollRepository lectureEnrollRepository;
    private final LectureCapacityRepository lectureCapacityRepository;
    private final UserService userService;

    // 특강 신청
    @Transient
    public void enrollLecture(Long userId, LectureEnrollRequestParam request) {
        // 데이터베이스로부터  userId 에 해당하는 유저 정보를 가져온다.
        // case 1. 회원이 존재하지 않을 경우
        // 해당 userId 에 해당하는 유저 정보가 존재하지 않을 경우, 회원이 존재하지 않는다는 에러 메시지를 반환한다.
        UserDto user = userService.findUserByUserId(userId);

        // lectureId 에 해당하는 특강 정보를 가져온다.
        // case 2. 특강이 존재하지 않을 경우
        // 해당 lectureId 에 해당하는 특강 정보가 존재하지 않을 경우, 특강이 존재하지 않는다는 에러 메시지를 반환한다.
        Lecture lecture = lectureRepository.findById(request.getLectureId()).orElseThrow(() -> {
            log.error("특강이 존재하지 않습니다.");
            return new RuntimeException("LECTURE_NOT_FOUND");
        });

        // lectureId에 해당하는 특강의 수강신청 인원을 가져온다.
        LectureCapacity lectureCapacity = lectureCapacityRepository.findById(request.getLectureId()).orElseThrow(() -> {
            log.error("특강 수강신청 인원이 존재하지 않습니다.");
            return new RuntimeException("LECTURE_CAPACITY_NOT_FOUND");
        });
        // case 3. 수강신청 인원이 30명 이상인 경우
        // 해당 lectureId에 해당하는 특강의 수강신청 인원이 30명 이상일 경우, 수강신청 인원이 초과되었다는 에러 메시지를 반환한다.
        if (lecture.getMaxStudentCount() == lectureCapacity.getCapacity()) {
            log.error("수강신청 인원이 초과되었습니다.");
            throw new RuntimeException("LECTURE_CAPACITY_EXCEEDED");
        }

        // userId와 lectureId를 이용하여 특강 신청정보를 가져온다.
        // case 4. 이미 신청한 경우
        // 해당 userId와 lectureId에 해당하는 특강 신청 정보가 이미 존재할 경우, 이미 신청한 특강이라는 에러 메시지를 반환한다.
        Optional<LectureEnrollmentList> lectureEnrollmentList = lectureEnrollRepository.findById(
                LectureEnrollmentId.builder()
                        .userId(user.getUserId())
                        .lectureId(lecture.getId())
                        .build());
        if (lectureEnrollmentList.isPresent()) {
            log.error("이미 신청한 특강입니다.");
            throw new RuntimeException("LECTURE_ALREADY_ENROLLED");
        }
        // 특강 신청정보를 저장한다.
        lectureEnrollRepository.save(LectureEnrollmentList.builder()
                .id(LectureEnrollmentId.builder()
                        .userId(user.getUserId())
                        .lectureId(lecture.getId())
                        .build())
                .enrolledAt(LocalDateTime.now())
                .build());
        // 수강신청 인원을 1명 증가시킨다.
        lectureCapacity.increaseEnrolledStudentCount(lectureCapacity.getCapacity() + 1);
    }
}
