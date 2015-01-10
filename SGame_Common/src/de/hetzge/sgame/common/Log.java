package de.hetzge.sgame.common;

import java.util.logging.Logger;

public final class Log {

	public static final String LOG_FOLDER = "log";

	public static final Logger NETWORK = Logger.getLogger("NETWORK");
	public static final Logger SYNC_MONITOR = LogUtil.createFileLogger("SYNC_MONITOR");
	public static final Logger PATH_FINDING = LogUtil.createFileLogger("PATH_FINDER");

	static {
	}

	private Log() {
	}

}
