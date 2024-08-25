package org.sundeep.logger.impl;

public class InfoLogProcessor extends LogProcessor {
	public InfoLogProcessor(LogProcessor logProcessor) {
		super(logProcessor);
	}

	@Override
	public void log(LogLevel logLevel, String message) {
		if (logLevel == LogLevel.INFO) {
			System.out.println("INFO: " + message);
			return;
		}
		super.log(logLevel, message);
	}
}
