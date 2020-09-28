package project.healthbox.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import project.healthbox.web.interceptors.FaviconInterceptor;

@Configuration
public class ApplicationWebConfiguration implements WebMvcConfigurer {

    private final FaviconInterceptor faviconInterceptor;

    @Autowired
    public ApplicationWebConfiguration(FaviconInterceptor faviconInterceptor) {
        this.faviconInterceptor = faviconInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(faviconInterceptor);
    }
}