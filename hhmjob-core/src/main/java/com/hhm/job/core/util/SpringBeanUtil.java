package com.hhm.job.core.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @Author huanghm
 * @Date 2022/12/23
 */
@Component
public class SpringBeanUtil implements ApplicationContextAware {
    private static ApplicationContext applicationContextSpring;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        applicationContextSpring = applicationContext;
    }

    /**
     * 通过class 获取Bean
     */
    public static <T> T getBean(Class<T> clazz) {
        return applicationContextSpring.getBean(clazz);
    }

}
