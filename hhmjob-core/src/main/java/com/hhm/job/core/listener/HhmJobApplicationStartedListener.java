package com.hhm.job.core.listener;

import com.hhm.job.core.health.HhmJobHealthCheckComponent;
import com.hhm.job.core.health.HhmJobRegisterComponent;
import com.hhm.job.core.health.HhmJobTaskClassFactory;
import com.hhm.job.core.task.HhmJobAbstractTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;

/**
 * springboot 启动后 执行
 *
 * @Author huanghm
 * @Date 2023/1/3
 */
@Slf4j
@Configuration
public class HhmJobApplicationStartedListener implements BeanPostProcessor, ApplicationContextAware, ApplicationListener<ApplicationStartedEvent> {
    private ApplicationContext applicationContext;

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        // 扫描到继承了HhmJobAbstractTask的类，并把其put进到map进行存储
        if (bean instanceof HhmJobAbstractTask) {
            HhmJobTaskClassFactory.putTask(bean.getClass().getName());
        }
        return bean;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        // 设置对象，避免相互依赖
        final HhmJobRegisterComponent hhmJobRegisterComponent = applicationContext.getBean(HhmJobRegisterComponent.class);
        final HhmJobHealthCheckComponent hhmJobHealthCheckComponent = applicationContext.getBean(HhmJobHealthCheckComponent.class);
        hhmJobRegisterComponent.setHhmJobHealthCheckComponent(hhmJobHealthCheckComponent);
        hhmJobHealthCheckComponent.setHhmJobRegisterComponent(hhmJobRegisterComponent);

        // 开启注册线程，往任务中心注册任务
        hhmJobRegisterComponent.startRegisterTasks();
    }
}
