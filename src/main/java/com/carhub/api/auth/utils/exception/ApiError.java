package com.carhub.api.auth.utils.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.http.HttpStatus;

import java.util.Calendar;
import java.util.Date;

public class ApiError {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date time = Calendar.getInstance().getTime();

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private String level;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private HttpStatus httpStatus;

    @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
    private int status;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private String exception;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private String message;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private String component;

    @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
    private int line;

    public ApiError() {}

    public ApiError(HttpStatus status) {
        this();
        this.httpStatus = status;
        this.status = this.httpStatus.value();
    }

    public ApiError(HttpStatus status, Throwable ex) {
        this();
        this.httpStatus = status;
        this.status = this.httpStatus.value();
        this.message = ex.getMessage();
        this.exception = ex.getClass().getName();
    }

    public ApiError(HttpStatus status, String level, Throwable ex) {
        this();
        StackTraceElement stack = ex.getStackTrace()[0];
        this.component = stack.getClassName() + "." + stack.getMethodName() + " (" + stack.getFileName() + ")";
        this.httpStatus = status;
        this.status = this.httpStatus.value();
        this.message = ex.getMessage();
        this.exception = ex.getClass().getName();
        this.level = level;
        this.line = stack.getLineNumber();
    }

    public HttpStatus getStatus(){
        return this.httpStatus;
    }
}
