package org.sundeep.logger.impl;

public abstract class LogProcessor {
	public enum LogLevel {
		INFO,
		DEBUG,
		ERROR
	}

	private final LogProcessor nextLogProcessor;

	public LogProcessor(LogProcessor nextLogProcessor) {
		this.nextLogProcessor = nextLogProcessor;
	}

	public void log(LogLevel logLevel, String message) {
		if (nextLogProcessor != null) {
			nextLogProcessor.log(logLevel, message);
		}
	}
}
