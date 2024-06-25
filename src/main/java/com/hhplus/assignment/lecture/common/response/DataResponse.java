package com.hhplus.assignment.lecture.common.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DataResponse<T> implements GenericResponse {

    protected final static DataResponse EMPTY_RESPONSE = new DataResponse(null);

    protected T data;

    protected DataResponse(T data) {
        this.data = data;
    }

    public static <T> DataResponse<T> create(T data) {
        return new DataResponse<>(data);
    }

    public static DataResponse getEmptyResponse() {
        return DataResponse.EMPTY_RESPONSE;
    }
}
