package de.hetzge.sgame.common;

import java.util.logging.Logger;

public final class LogUtil {

	private LogUtil() {
	}

	public static Logger createFileLogger(String name) {
		// try {
		Logger logger = Logger.getLogger(name);
		// FileHandler fileHandler = new FileHandler("log\\" +
		// name.toLowerCase() + ".log", 1000, 5);
		// fileHandler.setFormatter(new SimpleFormatter());
		// logger.addHandler(fileHandler);
		// logger.setUseParentHandlers(false);
		return logger;
		// } catch (SecurityException | IOException e) {
		// throw new IllegalStateException("Can't initalize logger", e);
		// }
	}

}
