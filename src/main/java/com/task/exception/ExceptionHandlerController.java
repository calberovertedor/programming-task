package com.task.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.RestClientException;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class ExceptionHandlerController {

	@ExceptionHandler({ ApiResponseNotValidException.class, SaveFileException.class, RestClientException.class,
			RuntimeException.class })
	public ResponseEntity<ApiErrorResponse> handleApiException(final Exception ex) {

		log.error("Error caused by: {}", ex.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(ApiErrorResponse.builder().error(ex.getClass().getSimpleName()).message(ex.getMessage()).build());
	}

}
