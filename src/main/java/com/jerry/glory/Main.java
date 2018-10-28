package com.jerry.glory;
import org.apache.logging.log4j.*;
public class Main {

	protected static final Logger parentLogger = LogManager.getLogger();


	private static Logger logger = parentLogger;
	public static void main(String []args) {
		logger.error("Test");

	}
}
