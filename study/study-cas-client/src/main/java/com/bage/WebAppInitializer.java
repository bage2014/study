package com.bage;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class WebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    
    protected Class<?>[] getRootConfigClasses() {
            // GolfingAppConfig defines beans that would be in root-context.xml
            return new Class[] { WebAppConfig.class };
    }

    
    protected Class<?>[] getServletConfigClasses() {
            // GolfingWebConfig defines beans that would be in golfing-servlet.xml
            return new Class[] { WebAppConfig.class };
    }

    
    protected String[] getServletMappings() {
            return new String[] { "/" };
    }

}
