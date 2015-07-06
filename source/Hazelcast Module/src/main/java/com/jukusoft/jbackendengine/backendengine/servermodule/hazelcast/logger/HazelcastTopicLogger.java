package com.jukusoft.jbackendengine.backendengine.servermodule.hazelcast.logger;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ITopic;
import com.jukusoft.jbackendengine.backendengine.logger.ILogger;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Justin on 02.07.2015.
 */
public class HazelcastTopicLogger implements ILogger {

    private ITopic<String> logger = null;
    private ITopic<String> errorLogger = null;
    private Long serverID = 0l;

    public HazelcastTopicLogger (HazelcastInstance hazelcastInstance, Long serverID, String loggerTopicName, String errorLoggerTopicName) {
        this.serverID = serverID;

        this.logger = hazelcastInstance.getTopic(loggerTopicName);
        this.errorLogger = hazelcastInstance.getTopic(errorLoggerTopicName);
    }

    public void writeToTopic (String str) {
        this.logger.publish(str);
    }

    public void writeToErrorTopic (String str) {
        this.errorLogger.publish(str);
    }

    @Override
    public void debug(String str) {
        Calendar calendar = new GregorianCalendar();
        str = "[Server " + this.serverID + ", " + calendar.getTime() + "] " + str;

        this.writeToTopic(str);
    }

    @Override
    public void warn(String str) {
        Calendar calendar = new GregorianCalendar();
        str = "[Server " + this.serverID + ", " + calendar.getTime() + "] " + str;

        this.writeToErrorTopic(str);
    }

    @Override
    public void error(String str) {
        Calendar calendar = new GregorianCalendar();
        str = "[Server " + this.serverID + ", " + calendar.getTime() + "] " + str;

        this.writeToErrorTopic(str);
    }
}
