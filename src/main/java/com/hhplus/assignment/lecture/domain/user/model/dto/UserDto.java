package com.hhplus.assignment.lecture.domain.user.model.dto;

import com.hhplus.assignment.lecture.domain.user.model.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserDto {

    private Long userId;
    private String name;

    public static UserDto of(User user) {
        return UserDto.builder()
                .userId(user.getId())
                .name(user.getName())
                .build();
    }
}
