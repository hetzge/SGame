package de.hetzge.sgame.common;

import java.util.logging.Level;
import java.util.logging.Logger;

public final class Log {

	public static final String LOG_FOLDER = "log";

	public static final Logger VOID = Logger.getLogger("VOID");
	public static final Logger LOG = Logger.getLogger("LOG");
	public static final Logger NETWORK = Logger.getLogger("NETWORK");
	public static final Logger PATHFINDING = Logger.getLogger("PATHFINDING");
	public static final Logger KI = Logger.getLogger("KI");
	public static final Logger SYNC_MONITOR = LogUtil.createFileLogger("SYNC_MONITOR");

	static {
		Log.VOID.setLevel(Level.OFF);
		Log.KI.setLevel(Level.OFF);
	}

	private Log() {
	}

}
