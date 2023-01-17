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
public class HhmJobLogFilter extends Filter<ILoggingEvent> {

    @Override
    public FilterReply decide(ILoggingEvent event) {
        final String loggerName = event.getLoggerName();
        final Object bean;
        try {
            bean = SpringBeanUtil.getBean(Class.forName(loggerName));
        } catch (ClassNotFoundException e) {
            log.error(e.getMessage(),e);
            return FilterReply.NEUTRAL;
        }
        if(!(bean instanceof HhmJobAbstractTask)) {
            return FilterReply.DENY;
        }
        log.info("需要记录日志,{}", event.getFormattedMessage());
        return FilterReply.NEUTRAL;
    }
}
