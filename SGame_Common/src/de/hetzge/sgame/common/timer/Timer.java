package de.hetzge.sgame.common.timer;

import java.io.Serializable;

public class Timer implements Serializable {

	private final long timespanInMs;
	private long lastCall = 0;

	public Timer(long timespanInMs) {
		super();
		this.timespanInMs = timespanInMs;
	}

	public boolean isTime() {
		long currentTimeInMs = System.currentTimeMillis();
		if (this.lastCall < currentTimeInMs) {
			this.lastCall = currentTimeInMs + this.timespanInMs;
			return true;
		}
		return false;
	}

	public long restTime() {
		long restTime = this.lastCall + this.timespanInMs - System.currentTimeMillis();
		if (restTime > 0) {
			return restTime;
		} else {
			return 0;
		}
	}

}
