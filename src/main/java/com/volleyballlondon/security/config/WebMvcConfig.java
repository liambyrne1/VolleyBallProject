package com.volleyballlondon.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import com.volleyballlondon.persistence.services.LeagueDbService;

@EnableWebMvc
@Configuration
@ComponentScan(basePackages = {"com.volleyballlondon.security",
                               "com.volleyballlondon.security.controller",
                               "com.volleyballlondon.dev",
                               "com.volleyballlondon.dev.validator"})
public class WebMvcConfig implements WebMvcConfigurer {

   @Bean
   public InternalResourceViewResolver resolver() {
      InternalResourceViewResolver resolver = new InternalResourceViewResolver();
      resolver.setViewClass(JstlView.class);
      resolver.setPrefix("/WEB-INF/views/");
      resolver.setSuffix(".jsp");
      return resolver;
   }

    /**
     * "/resources/**" allows js and css files to be loaded.
     */
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //Add css file resource url here
        registry
            .addResourceHandler("/resources/**")
            .addResourceLocations("resources/");
     }

    /**
      * Configure a handler to delegate unhandled requests by forwarding to the
      * Servlet container's "default" servlet. A common use case for this is when
      * the {@link DispatcherServlet} is mapped to "/" thus overriding the
      * Servlet container's default handling of static resources.
      */
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    @Bean
    public LeagueDbService leagueDbService() {
        return new LeagueDbService();
    }

}
