package com.cts.tdd.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingErrorMsgAndWarningLevels {

    private static final Logger logger = LoggerFactory.getLogger(LoggingErrorMsgAndWarningLevels.class);

    public static void main(String[] args) {
        logger.error("This is an error message");
        logger.warn("This is a warning message");
    }
}
