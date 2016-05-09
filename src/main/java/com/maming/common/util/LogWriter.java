package com.maming.common.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * User: maming
 * Date: 14-9-15
 */
public class LogWriter {

    private static final Logger errorLog = LogManager.getLogger("logger.error");
    private static final Logger commonLog = LogManager.getLogger("logger.common");

    private static final Logger otherLog = LogManager.getLogger("logger.other");
    private static final Logger shareLog = LogManager.getLogger("logger.share");

    public static Logger getErrorLog() {
        return errorLog;
    }

    public static Logger getCommonLog() {
        return commonLog;
    }

    public static Logger getOther() {
        return otherLog;
    }

    public static Logger getShare() {
        return shareLog;
    }
    
}

