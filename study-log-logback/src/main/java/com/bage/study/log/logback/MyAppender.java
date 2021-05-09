package com.bage.study.log.logback;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;

public class MyAppender extends AppenderBase<ILoggingEvent> {

    protected void append(ILoggingEvent iLoggingEvent) {
        if (iLoggingEvent != null && iLoggingEvent.getMessage() != null) {

            System.out.println(iLoggingEvent.getLevel().toString() + "-MyAppender-" + iLoggingEvent.getMessage());

        }
    }

}
