package com.hhplus.assignment.lecture.domain.lecture.model.dto;

import com.hhplus.assignment.lecture.exception.LectureException;
import lombok.Data;

@Data
public class LectureResponse<T> {

    private T data;
    private String message;

    public LectureResponse(T data, String message) {
        this.data = data;
        this.message = message;
    }

    public static <T> LectureResponse<T> of(T data, String message) {
        return new LectureResponse<>(data, message);
    }

    public Throwable getException() {
        return (LectureException) data;
    }
}
