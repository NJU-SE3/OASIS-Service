package com.example.oasisdocument.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class NormalAdvice {
	/**
	 * 用于资源不存在的异常返回
	 * 可以直接利用多态, 让NotFound这一类的异常继承自 EntityNotFoundException
	 * 统一处理 404
	 */
	@ResponseBody
	@ExceptionHandler(EntityNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	private String entityNotFoundHandler(EntityNotFoundException e) {
		return e.getMessage();
	}


	/**
	 * 400 Bad request
	 */
	@ResponseBody
	@ExceptionHandler(BadReqException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	private String badRequest(BadReqException e) {
		return "bad request";
	}


	/**
	 * 未知错误 500
	 */
	@ResponseBody
	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	private String exceptionHandler(Exception e) {
		e.printStackTrace();
		return e.getMessage();
	}
}
