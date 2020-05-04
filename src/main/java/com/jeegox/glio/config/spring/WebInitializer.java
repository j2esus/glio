package com.jeegox.glio.config.spring;

import com.jeegox.glio.config.general.SessionFilter;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.DispatcherServlet;

public class WebInitializer implements WebApplicationInitializer{

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        AnnotationConfigWebApplicationContext appContext = new AnnotationConfigWebApplicationContext();
        appContext.register(ApplicationContextConfig.class);
        
        ServletRegistration.Dynamic dispatcher = servletContext.addServlet("SpringDispatcher", new DispatcherServlet(appContext));
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");
        
        dispatcher.setInitParameter("contextClass", appContext.getClass().getName());
        servletContext.addListener(new ContextLoaderListener(appContext));
        
        FilterRegistration.Dynamic fr = servletContext.addFilter("encodingFilter", CharacterEncodingFilter.class);
        fr.setInitParameter("encoding", "UTF-8");
        fr.setInitParameter("forceEncoding", "true");
        fr.addMappingForUrlPatterns(null, true, "/*"); 
        
        FilterRegistration.Dynamic httpFr = servletContext.addFilter("HttpMethodFilter", HiddenHttpMethodFilter.class);
        httpFr.addMappingForUrlPatterns(null, true, "/*"); 
        
        FilterRegistration.Dynamic sessionFilter = servletContext.addFilter("SessionFilter", SessionFilter.class);
        sessionFilter.addMappingForUrlPatterns(null, true, "/*"); 
    }
}
