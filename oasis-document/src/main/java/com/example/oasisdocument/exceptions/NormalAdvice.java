package com.example.oasisdocument.exceptions;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
public class NormalAdvice {
    /**
     * 用于资源不存在的异常返回
     * 可以直接利用多态, 让NotFound这一类的异常继承自 EntityNotFoundException
     * 统一处理 404
     */
    @ResponseBody
    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(code = NOT_FOUND)
    private String entityNotFoundHandler(EntityNotFoundException e) {
        return NOT_FOUND.getReasonPhrase();
    }


    /**
     * 400 Bad request
     */
    @ResponseBody
    @ExceptionHandler(BadReqException.class)
    @ResponseStatus(code = BAD_REQUEST)
    private String badRequest(BadReqException e) {
        return BAD_REQUEST.getReasonPhrase();
    }


    /**
     * 未知错误 500
     */
    @ResponseBody
    @ExceptionHandler(Exception.class)
    @ResponseStatus(code = INTERNAL_SERVER_ERROR)
    private String exceptionHandler(Exception e) {
        return INTERNAL_SERVER_ERROR.getReasonPhrase();
    }
}
