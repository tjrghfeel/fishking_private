package com.tobe.fishking.v2.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringBeanUtils implements ApplicationContextAware
{

    private static ApplicationContext APPLICATION_CONTEXT;

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException
    {
        APPLICATION_CONTEXT = context;
    }

    public static Object getBean(String beanName)
    {
        return APPLICATION_CONTEXT.getBean(beanName);
    }

    public static <T> T getBean(String beanName, Class<T> requiredType)
    {
        return APPLICATION_CONTEXT.getBean(beanName, requiredType);
    }

    public static <T> T getBean(Class<T> type)
    {
        return APPLICATION_CONTEXT.getBean(type);
    }
}