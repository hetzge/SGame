package de.hetzge.sgame.common;

import java.util.logging.Logger;

public class Stopwatch {

	private final static Logger LOGGER = Logger.getLogger("STOPWATCH");

	private final String name;
	private final long startTime;
	private final long startNanoTime;

	public Stopwatch(String name) {
		this.name = name;
		this.startTime = System.currentTimeMillis();
		this.startNanoTime = System.nanoTime();
	}

	public void stop() {
		Stopwatch.LOGGER.info(this.name + ": " + (System.currentTimeMillis() - this.startTime) + " / " + (System.nanoTime() - this.startNanoTime));
	}

}
