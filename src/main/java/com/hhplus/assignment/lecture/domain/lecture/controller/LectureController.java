package com.hhplus.assignment.lecture.domain.lecture.controller;

import com.hhplus.assignment.lecture.common.response.DataResponse;
import com.hhplus.assignment.lecture.common.response.GenericResponse;
import com.hhplus.assignment.lecture.domain.lecture.model.param.CheckEnrollCourseRequestParam;
import com.hhplus.assignment.lecture.domain.lecture.model.param.LectureEnrollRequestParam;
import com.hhplus.assignment.lecture.domain.lecture.model.param.LectureOpenCourseSearchRequestParam;
import com.hhplus.assignment.lecture.domain.lecture.service.LectureService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/lectures")
public class LectureController {

    private final LectureService lectureService;

    /*
     * 1. 특강 신청
     *
     * 특정 userId 로 선착순으로 제공되는 특강 신청
     * 동일한 신청자는 한번의 수강신청만 성공 가능
     * 각 강의는 선착순 30명
     * 이미 신청자가 30명 초과되면 이후 신청자는 요청 실패
     * 어떤 유저가 특강을 신청했는지 히스토리 필요
     * */
    @PostMapping("/{userId}")
    public GenericResponse enrollLecture(@PathVariable Long userId, @RequestBody LectureEnrollRequestParam param) {
        // 특강 신청
        return DataResponse.create(lectureService.enrollLecture(userId, param));
    }

    /*
    * 2. 특강 목록
    * */
    @GetMapping("")
    public GenericResponse getLectureCourses(LectureOpenCourseSearchRequestParam param) {
        // 특강 목록 조회
        return DataResponse.create(lectureService.openCourseList(param));
    }


    /*
     * 3. 특강 신청완료 확인
     * */
    @GetMapping("/enroll/{userId}")
    public GenericResponse getLectureEnrollHistory(@PathVariable Long userId, CheckEnrollCourseRequestParam param) {
        // 특강 신청 내역 조회
        return DataResponse.create(lectureService.checkEnrolledCourse(userId, param));
    }
}

