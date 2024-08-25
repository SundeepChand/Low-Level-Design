package org.sundeep.logger.impl;

public class DebugLogProcessor extends LogProcessor {
	public DebugLogProcessor(LogProcessor logProcessor) {
		super(logProcessor);
	}

	@Override
	public void log(LogLevel logLevel, String message) {
		if (logLevel == LogLevel.DEBUG) {
			System.out.println("DEBUG: " + message);
			return;
		}
		super.log(logLevel, message);
	}
}
