package com.hhplus.assignment.lecture.domain.lecture.controller;


import com.hhplus.assignment.lecture.common.response.GenericResponse;
import com.hhplus.assignment.lecture.domain.lecture.model.dto.LectureEnrollHistoryDto;
import com.hhplus.assignment.lecture.domain.lecture.model.dto.LectureResponse;
import com.hhplus.assignment.lecture.domain.lecture.model.dto.OpenCourseDto;
import com.hhplus.assignment.lecture.domain.lecture.model.entity.LectureEnrollmentId;
import com.hhplus.assignment.lecture.domain.lecture.model.param.CheckEnrollCourseRequestParam;
import com.hhplus.assignment.lecture.domain.lecture.model.param.LectureEnrollRequestParam;
import com.hhplus.assignment.lecture.domain.lecture.model.param.OpenCourseResisterRequestParam;
import com.hhplus.assignment.lecture.domain.lecture.repository.LectureEnrollHistoryRepository;
import com.hhplus.assignment.lecture.domain.lecture.repository.OpenCourseRepository;
import com.hhplus.assignment.lecture.exception.LectureException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LectureControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private OpenCourseRepository openCourseRepository;

    @Autowired
    private LectureEnrollHistoryRepository lectureEnrollRepository;

    @AfterEach
    public void tearDown() {
        openCourseRepository.deleteAllCourses();
    }

    @Test
    @DisplayName("특강 신청 - 이미 신청한 특강을 다시 신청하려고 할 때, 이미 신청한 특강이라는 에러 메시지를 반환한다.")
    void alreadyEnrollLecture() {
        // given
        long userId = 1L;
        OpenCourseDto openCourse = openCourseRepository.registerCourse(OpenCourseResisterRequestParam.builder()
                .lectureId(1L)
                .maxStudentCount(30)
                .capacity(0)
                .openAt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build());
        lectureEnrollRepository.enrollCourse(userId, openCourse.getCourseId());
        LectureEnrollRequestParam requestParam = LectureEnrollRequestParam.builder()
                .courseId(openCourse.getCourseId())
                .build();

        String url = "http://localhost:" + port + "/lectures/" + userId;

        // when
        ResponseEntity<LectureEnrollHistoryDto> response = restTemplate.postForEntity(url, requestParam, LectureEnrollHistoryDto.class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThatExceptionOfType(RuntimeException.class).isThrownBy(() -> {
            throw new LectureException(HttpStatus.BAD_REQUEST, "ALREADY_ENROLLED", "이미 수강신청한 강의입니다.");
        });
    }

    @Test
    @DisplayName("특강 신청완료 확인 - 특강 신청이 완료되었는지 확인한다.")
    void checkEnrolledCourse() {
        // given
        long userId = 1L;
        long courseId = 1L;
        LectureEnrollHistoryDto lectureEnrollHistoryDto = lectureEnrollRepository.enrollCourse(userId, courseId);

        String url = "http://localhost:" + port + "/lectures/enroll/" + userId+"?courseId={courseId}";
        Map<String, String> params = Map.of("courseId", String.valueOf(courseId));

        // when
        ResponseEntity<LectureEnrollHistoryDto> response = restTemplate.getForEntity(url, LectureEnrollHistoryDto.class, params);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("특강 신청 - 순차적으로 특강을 신청한다.")
    void sequentialEnrollCourseTest() {

        // given
        int userCount = 40;
        LocalDateTime openAt = LocalDateTime.of(2024, 6, 28, 10, 0, 0);

        OpenCourseDto openCourse = openCourseRepository.registerCourse(OpenCourseResisterRequestParam.builder()
                .lectureId(1L)
                .maxStudentCount(30)
                .capacity(0)
                .openAt(openAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build());

        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failCount = new AtomicInteger(0);

        // when
        for (int i = 0; i < userCount; i++) {
            long userId = i;
            long courseId = openCourse.getCourseId();
            LectureEnrollRequestParam requestParam = LectureEnrollRequestParam.builder()
                    .courseId(courseId)
                    .build();

            String url = "http://localhost:" + port + "/lectures/" + userId;

            ResponseEntity<LectureEnrollHistoryDto> response = restTemplate.postForEntity(url, requestParam, LectureEnrollHistoryDto.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                successCount.incrementAndGet();
            } else {
                failCount.incrementAndGet();
            }
        }

        System.out.println("successCount = " + successCount);
        System.out.println("failCount = " + failCount);

        // then
        assertThat(successCount.get()).isEqualTo(30);
        assertThat(failCount.get()).isEqualTo(10);
    }

    @Test
    @DisplayName("특강 신청 - 선착순으로 먼저 신청한 유저가 신청이 성공하고, 정원이 가득차면 후순위 유저는 실패한다.")
    void firstComeFirstEnrollCourseTest() throws InterruptedException {

        // given
        int userCount = 40;
        LocalDateTime openAt = LocalDateTime.of(2024, 6, 28, 10, 0, 0);

        OpenCourseDto openCourse = openCourseRepository.registerCourse(OpenCourseResisterRequestParam.builder()
                .lectureId(1L)
                .maxStudentCount(30)
                .capacity(0)
                .openAt(openAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build());

        ExecutorService executorService = Executors.newFixedThreadPool(userCount);
        CountDownLatch latch = new CountDownLatch(userCount);

        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failCount = new AtomicInteger(0);

        // when
        List<Integer> enrollSuccessUserList = new ArrayList<>();
        for (int i = 0; i < userCount; i++) {
            int finalI = i;
            executorService.submit(() -> {
                try {
                    long userId = finalI;
                    long courseId = openCourse.getCourseId();
                    LectureEnrollRequestParam requestParam = LectureEnrollRequestParam.builder()
                            .courseId(courseId)
                            .build();

                    String url = "http://localhost:" + port + "/lectures/" + userId;

                    ResponseEntity<LectureEnrollHistoryDto> response = restTemplate.postForEntity(url, requestParam, LectureEnrollHistoryDto.class);

                    if (response.getStatusCode().is2xxSuccessful()) {
                        enrollSuccessUserList.add(finalI);
                        successCount.incrementAndGet();
                    } else {
                        failCount.incrementAndGet();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();

        System.out.println("successCount = " + successCount);
        System.out.println("failCount = " + failCount);

        // then
        OpenCourseDto course = openCourseRepository.findById(openCourse.getCourseId());
        for(Integer userId : enrollSuccessUserList) {
            LectureEnrollHistoryDto lectureEnrollHistoryDto = lectureEnrollRepository.findByEnrollmentId(LectureEnrollmentId.builder()
                    .userId(userId.longValue())
                    .courseId(course.getCourseId())
                    .build());

            assertThat(lectureEnrollHistoryDto).isNotNull();
        }

        assertThat(successCount.get()).isEqualTo(30);
        assertThat(failCount.get()).isEqualTo(10);
        assertThat(course.getCapacity()).isEqualTo(30);

    }
}
