package com.jukusoft.jbackendengine.logger.impl;

import com.jukusoft.jbackendengine.logger.ILogger;
import com.jukusoft.jbackendengine.logger.ILoggerManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Justin on 02.07.2015.
 */
public class DefaultLoggerManager implements ILoggerManager {

    private List<ILogger> loggerList = new ArrayList<ILogger>();

    @Override
    public void debug(String str) {
        List<ILogger> copyList = Collections.synchronizedList(this.loggerList);

        for (ILogger logger : copyList) {
            logger.debug(str);
        }
    }

    @Override
    public void warn(String str) {
        List<ILogger> copyList = Collections.synchronizedList(this.loggerList);

        for (ILogger logger : copyList) {
            logger.warn(str);
        }
    }

    @Override
    public void error(String str) {
        List<ILogger> copyList = Collections.synchronizedList(this.loggerList);

        for (ILogger logger : copyList) {
            logger.error(str);
        }
    }

    @Override
    public void registerLogger(ILogger logger) {
        this.loggerList.add(logger);
    }

    @Override
    public void removeLogger(ILogger logger) {
        this.loggerList.remove(logger);
    }
}
