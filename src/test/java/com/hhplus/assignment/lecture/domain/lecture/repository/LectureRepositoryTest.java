package com.hhplus.assignment.lecture.domain.lecture.repository;


import com.hhplus.assignment.lecture.domain.lecture.model.entity.Lecture;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class LectureRepositoryTest {

    @Autowired
    private LectureRepository lectureRepository;

    /*
    * 1. 특강 신청기능
    * 2. 특강 목록 조회기능
    * 3. 특강 신청완료 확인기능
    * */

    // 테스트 데이터 삽입
    @BeforeEach
    public void setUp() {
        lectureRepository.save(Lecture.builder()
                .title("특강1")
                .maxStudentCount(30)
                .build());
    }

    /*
    * 1. 특강 신청기능
    *
    * 특정 userId 로 선착순으로 제공되는 특강 신청
    * 동일한 신청자는 한번의 수강신청만 성공 가능
    * 각 강의는 선착순 30명
    * 이미 신청자가 30명 초과되면 이후 신청자는 요청 실패
    * 어떤 유저가 특강을 신청했는지 히스토리 필요
    *
    * */

    @Test
    @DisplayName("수강신청한 특강이 존재하지 않을 때")
    void enrollLectureNotExisted() {
        // given


        // when


        // then
    }

}
