package com.wujiahui.forum.logging;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

/**
 * 进程ID 获取
 *
 * Created by NgCafai on 2019/8/16 12:07.
 */
public class ProcessIdClassicConverter extends ClassicConverter {

    /**
     * (non-Javadoc)
     *
     * @see ch.qos.logback.core.pattern.Converter#convert(java.lang.Object)
     */
    public String convert(ILoggingEvent event) {
        RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
        String name = runtime.getName();
        return name.substring(0, name.indexOf("@"));
    }

}
