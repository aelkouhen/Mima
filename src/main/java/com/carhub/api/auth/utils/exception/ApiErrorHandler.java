package com.carhub.api.auth.utils.exception;

import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.net.BindException;

@Order(0)
@ControllerAdvice
public class ApiErrorHandler {

    @ExceptionHandler({
            Exception.class,
            ElementNotFoundException.class,
            ElementNotCreatedException.class,
            ElementNotUpdatedException.class,
            ElementNotDeletedException.class,
            HttpRequestMethodNotSupportedException.class,
            HttpMediaTypeNotSupportedException.class,
            HttpMediaTypeNotAcceptableException.class,
            MissingPathVariableException.class,
            MissingServletRequestParameterException.class,
            ServletRequestBindingException.class,
            ConversionNotSupportedException.class,
            TypeMismatchException.class,
            HttpMessageNotReadableException.class,
            HttpMessageNotWritableException.class,
            MethodArgumentNotValidException.class,
            MissingServletRequestPartException.class,
            BindException.class,
            NoHandlerFoundException.class,
            AsyncRequestTimeoutException.class
    })
    public ResponseEntity<ApiError> handleAll(Throwable ex, WebRequest request){
        if(ex instanceof ElementNotFoundException) return buildResponseEntity(new ApiError(HttpStatus.NOT_FOUND,"error" , ex));
        else if(ex instanceof ElementNotCreatedException) return buildResponseEntity(new ApiError(HttpStatus.EXPECTATION_FAILED,"error" , ex));
        else if(ex instanceof ElementNotUpdatedException) return buildResponseEntity(new ApiError(HttpStatus.EXPECTATION_FAILED,"error" , ex));
        else if(ex instanceof ElementNotDeletedException) return buildResponseEntity(new ApiError(HttpStatus.FORBIDDEN,"error" , ex));
        else if(ex instanceof HttpRequestMethodNotSupportedException) return buildResponseEntity(new ApiError(HttpStatus.METHOD_NOT_ALLOWED,"error" , ex));
        else if(ex instanceof HttpMediaTypeNotSupportedException) return buildResponseEntity(new ApiError(HttpStatus.UNSUPPORTED_MEDIA_TYPE,"error" , ex));
        else if(ex instanceof HttpMediaTypeNotAcceptableException) return buildResponseEntity(new ApiError(HttpStatus.NOT_ACCEPTABLE,"error" , ex));
        else if(ex instanceof MissingPathVariableException) return buildResponseEntity(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR,"error" , ex));
        else if(ex instanceof MissingServletRequestParameterException) return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST,"error" , ex));
        else if(ex instanceof ServletRequestBindingException) return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST,"error" , ex));
        else if(ex instanceof ConversionNotSupportedException) return buildResponseEntity(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR,"error" , ex));
        else if(ex instanceof TypeMismatchException) return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST,"error" , ex));
        else if(ex instanceof HttpMessageNotReadableException) return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST,"error" , ex));
        else if(ex instanceof HttpMessageNotWritableException) return buildResponseEntity(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR,"error" , ex));
        else if(ex instanceof MethodArgumentNotValidException) return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST,"error" , ex));
        else if(ex instanceof MissingServletRequestPartException) return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST,"error" , ex));
        else if(ex instanceof BindException) return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST,"error" , ex));
        else if(ex instanceof NoHandlerFoundException) return buildResponseEntity(new ApiError(HttpStatus.NOT_FOUND,"error" , ex));
        else if(ex instanceof AsyncRequestTimeoutException) return buildResponseEntity(new ApiError(HttpStatus.SERVICE_UNAVAILABLE,"error" , ex));

        return buildResponseEntity(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR,"error" , ex));
    }

    private ResponseEntity<ApiError> buildResponseEntity(ApiError error) {
        return new ResponseEntity<ApiError>(error, new HttpHeaders(), error.getStatus());
    }
}
