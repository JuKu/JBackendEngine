package com.jukusoft.jbackendengine.logger;

import com.jukusoft.jbackendengine.manager.IManager;

/**
 * Created by Justin on 02.07.2015.
 */
public interface ILoggerManager extends IManager {

    public void debug (String str);
    public void warn (String str);
    public void error (String str);

    public void registerLogger (ILogger logger);
    public void removeLogger (ILogger logger);

}
