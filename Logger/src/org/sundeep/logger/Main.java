package org.sundeep.logger;

import org.sundeep.logger.impl.DebugLogProcessor;
import org.sundeep.logger.impl.ErrorLogProcessor;
import org.sundeep.logger.impl.InfoLogProcessor;
import org.sundeep.logger.impl.LogProcessor;

public class Main {
	public static void main(String[] args) {
		LogProcessor logger = new ErrorLogProcessor(new InfoLogProcessor(new DebugLogProcessor(null)));

		logger.log(LogProcessor.LogLevel.DEBUG, "hello world!");
		logger.log(LogProcessor.LogLevel.INFO, "hello world!");
		logger.log(LogProcessor.LogLevel.ERROR, "hello world!");
	}
}
