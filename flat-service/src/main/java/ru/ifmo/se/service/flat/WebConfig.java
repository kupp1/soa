package ru.ifmo.se.service.flat;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.resource.PathResourceResolver;
import ru.ifmo.se.common.AbstractWebConfig;

@Configuration
public class WebConfig extends AbstractWebConfig {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/api/v1/**").addResourceLocations("/");
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/api/v1/swagger-ui/")
                .setViewName("forward:/api/v1/swagger-ui/index.html");
    }
}
