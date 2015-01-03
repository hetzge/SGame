package de.hetzge.sgame.common.exception;

public class SomeException extends RuntimeException {

	public SomeException() {
		super();
	}

	public SomeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public SomeException(String message, Throwable cause) {
		super(message, cause);
	}

	public SomeException(String message) {
		super(message);
	}

	public SomeException(Throwable cause) {
		super(cause);
	}
}
