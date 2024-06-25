package com.hhplus.assignment.lecture.domain.user.model.service;

import com.hhplus.assignment.lecture.domain.user.model.dto.UserDto;
import com.hhplus.assignment.lecture.domain.user.model.entity.User;
import com.hhplus.assignment.lecture.domain.user.repository.UserRepository;
import com.hhplus.assignment.lecture.exception.UserException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserDto findUserByUserId (Long userId) {
        // 데이터베이스로부터 userId에 해당하는 유저 정보를 가져온다.
        User user = userRepository.findById(userId).orElseThrow(() -> {
            // case 1. 회원이 존재하지 않을 경우
            log.error("회원이 존재하지 않습니다.");
            // 해당 userId에 해당하는 유저 정보가 존재하지 않을 경우, 회원이 존재하지 않는다는 에러 메시지를 반환한다.
            return new UserException(HttpStatus.NOT_FOUND, "USER_NOT_FOUND", "회원이 존재하지 않습니다.");
        });

        // userId에 해당하는 유저 정보를 UserDto로 변환하여 반환한다.
        return UserDto.of(user);
    }
}
