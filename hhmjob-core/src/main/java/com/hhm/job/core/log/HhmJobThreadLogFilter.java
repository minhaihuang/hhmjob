package com.hhm.job.core.log;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;
import com.hhm.job.core.task.HhmJobAbstractTask;
import com.hhm.job.core.util.SpringBeanUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author huanghm
 * @Date 2023/1/16
 */
@Slf4j
public class HhmJobThreadLogFilter extends Filter<ILoggingEvent> {
    private String targetThreadName;

    public HhmJobThreadLogFilter(String targetThreadName) {
        this.targetThreadName = targetThreadName;
    }

    @Override
    public FilterReply decide(ILoggingEvent event) {
        final String threadName = event.getThreadName();
        if(!targetThreadName.equals(threadName)){
            return FilterReply.DENY;
        }
        return FilterReply.NEUTRAL;
    }
}
