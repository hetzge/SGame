package de.hetzge.sgame.common;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Date;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public final class LogUtil {

	private static class LogFormatter extends Formatter {

		private static final String LINE_SEPARATOR = System.getProperty("line.separator");

		@Override
		public String format(LogRecord record) {
			StringBuilder sb = new StringBuilder();

			sb.append(new Date(record.getMillis())).append(" ").append(record.getLevel().getLocalizedName()).append(" ").append(record.getLoggerName()).append(": ").append(this.formatMessage(record))
					.append(LogFormatter.LINE_SEPARATOR);

			if (record.getThrown() != null) {
				try {
					StringWriter sw = new StringWriter();
					PrintWriter pw = new PrintWriter(sw);
					record.getThrown().printStackTrace(pw);
					pw.close();
					sb.append(sw.toString());
				} catch (Exception ex) {
				}
			}

			return sb.toString();
		}
	}

	private static final LogFormatter FORMATTER = new LogFormatter();

	private LogUtil() {
	}

	public static Logger createFileLogger(String name) {
		try {
			FileHandler fileHandler = new FileHandler("log/" + name + ".log");
			fileHandler.setFormatter(LogUtil.FORMATTER);
			Logger logger = Logger.getLogger(name);
			logger.setUseParentHandlers(false);
			logger.addHandler(fileHandler);
			return logger;
		} catch (SecurityException | IOException e) {
			Log.LOG.warning("can't create file logger with name " + name);
		}
		return Log.VOID;
	}
}
