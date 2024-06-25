package com.hhplus.assignment.lecture.exception.handler;

import com.hhplus.assignment.lecture.common.response.ErrorResponse;
import com.hhplus.assignment.lecture.exception.model.ErrorResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Throwable.class)
    public ErrorResponse unHandledException(Exception e) {
        log.error("unHandledException", e);
        return ErrorResponse.create(ErrorResult.builder()
                .code("INTERNAL_SERVER_ERROR")
                .message("서버 에러가 발생했습니다.")
                .build());
    }

}
