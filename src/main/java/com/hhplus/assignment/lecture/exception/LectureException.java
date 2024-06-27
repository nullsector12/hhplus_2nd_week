package com.hhplus.assignment.lecture.exception;

import com.hhplus.assignment.lecture.common.exception.GenericException;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

@Data
@EqualsAndHashCode(callSuper = true)
public class LectureException extends GenericException {

    private final HttpStatus httpStatus;
    private final String code;
    private String message;

    public LectureException(HttpStatus httpStatus, String code, String message) {
        super(message);
        this.httpStatus = httpStatus;
        this.code = code;
    }
}
