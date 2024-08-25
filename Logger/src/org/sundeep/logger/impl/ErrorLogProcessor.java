package org.sundeep.logger.impl;

public class ErrorLogProcessor extends LogProcessor {
	public ErrorLogProcessor(LogProcessor logProcessor) {
		super(logProcessor);
	}

	@Override
	public void log(LogLevel logLevel, String message) {
		if (logLevel == LogLevel.ERROR) {
			System.out.println("ERROR: " + message);
			return;
		}
		super.log(logLevel, message);
	}
}
