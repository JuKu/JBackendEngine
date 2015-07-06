package com.jukusoft.jbackendengine.backendengine.logger.impl;

import com.jukusoft.jbackendengine.backendengine.logger.ILogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Justin on 02.07.2015.
 */
public class OutputLogger implements ILogger {

    private Logger logger = null;

    public OutputLogger () {
        this.logger = LoggerFactory.getLogger(OutputLogger.class);
    }

    @Override
    public void debug(String str) {
        this.logger.debug(str);
    }

    @Override
    public void warn(String str) {
        this.logger.warn(str);
    }

    @Override
    public void error(String str) {
        this.logger.error(str);
    }
}
