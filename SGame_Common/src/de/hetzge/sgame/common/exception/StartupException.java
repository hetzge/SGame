package de.hetzge.sgame.common.exception;

public class StartupException extends RuntimeException {

	public StartupException() {
		super();
	}

	public StartupException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

	public StartupException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public StartupException(String arg0) {
		super(arg0);
	}

	public StartupException(Throwable arg0) {
		super(arg0);
	}

}
