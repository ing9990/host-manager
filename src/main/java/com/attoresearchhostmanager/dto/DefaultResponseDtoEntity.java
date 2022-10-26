package com.attoresearchhostmanager.dto;

/**
 * @author Taewoo
 */


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DefaultResponseDtoEntity {

    private String message;

    private Object data;

    private String path;

    private HttpStatus httpStatus;

    public static DefaultResponseDtoEntity of(HttpStatus httpStatus, String message) {
        return DefaultResponseDtoEntity.builder().httpStatus(httpStatus).message(message).build();
    }

    public static DefaultResponseDtoEntity of(HttpStatus httpStatus, String message, Object data) {
        return DefaultResponseDtoEntity.builder().httpStatus(httpStatus).message(message).data(data).build();
    }

    public static DefaultResponseDtoEntity of(HttpStatus httpStatus, String message, Object data, String path) {
        return DefaultResponseDtoEntity.builder().httpStatus(httpStatus).message(message).data(data).path(path).build();
    }

    public static DefaultResponseDtoEntity ok(String message) {
        return DefaultResponseDtoEntity.builder().httpStatus(HttpStatus.OK).message(message).build();
    }

    public static DefaultResponseDtoEntity ok(String message, Object data) {
        return DefaultResponseDtoEntity.builder().httpStatus(HttpStatus.OK).message(message).data(data).build();
    }

    public static DefaultResponseDtoEntity ok(String message, Object[] data) {
        return DefaultResponseDtoEntity.builder().httpStatus(HttpStatus.OK).message(message).data(data).build();
    }
}
