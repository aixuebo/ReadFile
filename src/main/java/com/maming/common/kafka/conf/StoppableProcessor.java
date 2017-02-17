package com.maming.common.kafka.conf;

/**
 * Created on 2015-1-21.
 */
public interface StoppableProcessor {

    public void start() throws Exception;

    public void shutdown() throws Exception;

}
