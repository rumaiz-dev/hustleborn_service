package com.hustleborn.service.utils.exceptions;

import org.springframework.http.HttpStatus;

public class ApiException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	private final Object error;
	private final HttpStatus status;

	public ApiException(String message, Object error, HttpStatus status) {
		super(message);
		this.error = error;
		this.status = status;
	}

	public Object getError() {
		return error;
	}

	public HttpStatus getStatus() {
		return status;
	}
}
