package de.hetzge.sgame.common;

public abstract class Context<TYPE> {

	private final ThreadLocal<TYPE> threadLocal = new ThreadLocal<>();

	public void set(TYPE contextObject) {
		this.threadLocal.set(contextObject);
	}

	public TYPE get() {
		TYPE contextObject = this.threadLocal.get();
		if (contextObject == null) {
			throw new IllegalAccessError("Context not available");
		}
		return contextObject;
	}

}
