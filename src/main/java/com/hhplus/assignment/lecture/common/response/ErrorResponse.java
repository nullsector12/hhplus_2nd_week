package com.hhplus.assignment.lecture.common.response;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.hhplus.assignment.lecture.exception.model.ErrorResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse implements GenericResponse{

    @JsonUnwrapped
    private ErrorResult error;

    public static ErrorResponse create(String code, String message) {
        return new ErrorResponse(ErrorResult.builder()
            .code(code)
            .message(message)
            .build());
    }

    public static ErrorResponse create(ErrorResult error) {
        return new ErrorResponse(error);
    }

}
