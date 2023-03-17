package ru.derendyaev.SpringMVC.config;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class MySpringMvcDispatcherInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[0];
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[] {SpringConfig.class};
    }// конфигурация в классе SpringConfig.java

    @Override
    protected String[] getServletMappings() {
        return new String[] {"/"};
    }// все http запросы возвращаются по "/"
}
