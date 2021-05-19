package com.task.exception;

public class ApiResponseNotValidException extends Exception {

	private static final long serialVersionUID = -4605979434174359771L;

	public ApiResponseNotValidException(String message) {
		super(message);
	}
	public ApiResponseNotValidException(String message, Throwable cause) {
		super(message, cause);
	}
}
