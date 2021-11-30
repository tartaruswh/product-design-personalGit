package com.bewg.pd.workbook.utils;

import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 自定义上下文组件
 * </p>
 *
 * @author tianzhitao
 * @since 2021-10-22
 */
@Component
@Lazy(value = false)
public class ApplicationContextUtil implements ApplicationContextAware {

    private static ApplicationContext context = null;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    /**
     * 根据beanName获取组件
     *
     * @param name
     * @return
     */
    public static Object getBean(String name) {
        return context.getBean(name);
    }

    /**
     * 根据beanClass获取组件
     *
     * @param clazz
     * @return
     */
    public static Object getBean(Class clazz) {
        return context.getBean(clazz);
    }

    /**
     * 获取当前类实例
     *
     * @return
     */
    public static ApplicationContextUtil getInstance() {
        return (ApplicationContextUtil)context.getBean("applicationContextUtil");
    }

    /**
     * AOP的代理情况下,返回被代理的本来的Bean.
     *
     * @param bean
     *            bean
     * @return 对象
     * @throws Exception
     *             异常
     */
    public static final Object unwrapProxy(final Object bean) throws Exception {
        Object beanReal = bean;
        if (AopUtils.isAopProxy(bean) && bean instanceof Advised) {
            Advised advised = (Advised)bean;
            beanReal = advised.getTargetSource().getTarget();
        }
        return beanReal;
    }

    /**
     * 获取当前运行环境
     *
     * @return
     */
    public static String getActiveProfile() {
        return context.getEnvironment().getActiveProfiles()[0];
    }

}