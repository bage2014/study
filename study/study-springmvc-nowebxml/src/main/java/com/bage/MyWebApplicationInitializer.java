package com.bage;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class MyWebApplicationInitializer extends AbstractAnnotationConfigDispatcherServletInitializer{

        
        protected Class<?>[] getRootConfigClasses() {
                return null;
        }

        
        protected Class<?>[] getServletConfigClasses() {
                return new Class[] { MyWebApplicationConfig.class };
        }

        
        protected String[] getServletMappings() {
                return new String[] { "/" };
        }

        // If using XML-based Spring configuration, you should extend directly from AbstractDispatcherServletInitializer:
        protected WebApplicationContext createServletApplicationContext() {
                XmlWebApplicationContext cxt = new XmlWebApplicationContext();
                cxt.setConfigLocation("/WEB-INF/spring-servlet.xml");
                return cxt;
        }
}
