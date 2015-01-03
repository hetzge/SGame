package de.hetzge.sgame.common.exception;

public class NetworkException extends RuntimeException {

	public NetworkException() {
		super();
	}

	public NetworkException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

	public NetworkException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public NetworkException(String arg0) {
		super(arg0);
	}

	public NetworkException(Throwable arg0) {
		super(arg0);
	}

}
